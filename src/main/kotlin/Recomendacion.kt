package AppDeLibros

class Recomendacion(
    var nombre: String,
    var publico: Boolean,
    val creador: Usuario,
    val librosRecomendados: MutableList<Libro>,
    var detalle: String,                            // Esta es la reseña
    var contribuciones: MutableMap<Usuario, MutableList<Libro>> = mutableMapOf(),
    var contribucionesLimite: Int,
):RepositorioDatos{

    override var id: Int = 0

    override fun searchCondition(texto: String): Boolean {
        return coincideCreadorApellido(texto) || coincideTitulosResena(texto)
    }
    private fun coincideCreadorApellido(texto: String): Boolean {
        return (creador.apellido.equals(texto, ignoreCase = true))
    }

    private fun coincideTitulosResena(texto: String): Boolean{
        return librosRecomendados.any{it.coincideTitulo(texto)}
    }

    //------------------------- VALORACIONES -------------------------//
    val valoraciones: MutableList<Valoracion> = mutableListOf()

    fun agregarValoracion(usuario: Usuario, valor: Double, comentario: String) {
        require(valor in 1.0..5.0)
        val valoracionDelUsuario = valoraciones.find {it.usuario == usuario}

        if (existeValoracion(usuario)) {
            valoracionDelUsuario?.editar(valor, comentario)
        } else {
            if (puedeValorar(usuario)) {
                valoraciones.add(Valoracion(usuario, valor, comentario))
            } else {
                println("El usuario no cumple con los requisitos para valorar esta recomendación.")
            }
        }
    }

    fun calcularValoracionPromedio(): Double {
        if (valoraciones.isEmpty()) {
            return 0.0
        }
        val totalValoraciones = valoraciones.size
        val sumaValoraciones = valoraciones.sumOf { it.valor }
        return sumaValoraciones.toDouble() / totalValoraciones
    }

    private fun puedeValorar(usuario: Usuario): Boolean {
        return usuario != creador && requerimientosDeValoracion(usuario)
    }

    private fun requerimientosDeValoracion(usuario: Usuario): Boolean {
        return usuario.leyoLibrosRecomendados(librosRecomendados) || losLibrosSonDelMismoAutorYEsPreferido(usuario)
    }

    // CHEKEA PRIMERO SI TODOS LOS LIBROS FUERON ESCRITOS POR EL MISMO AUTOR, LUEGO SI ES UN AUTOR PREFERIDO
    private fun losLibrosSonDelMismoAutorYEsPreferido(usuario: Usuario): Boolean{
        return if (sonDelMismoAutor(librosRecomendados)){
            val autorUnico = listaDeAutores(librosRecomendados).first()
            autorEsPreferido(usuario, autorUnico)
        } else {
            false
        }
    }

    // DEVUELVE LOS AUTORES DE TODOS LOS LIBROS (MUY IMPORTANTE)
    private fun listaDeAutores(libros: List<Libro>): List<Autor>{
        return libros.map { it.autor }
    }

    // CHECKEA QUE TODOS LOS LIBROS DE LA RECOMENDACION SEAN DEL MISMO AUTOR
    private fun sonDelMismoAutor(libros: List<Libro>): Boolean {
        return listaDeAutores(librosRecomendados).distinct().size == 1
    }

    // CHECKEA QUE EL AUTOR SEA UN AUTOR PREFERIDO DEL USUARIO
    private fun autorEsPreferido(usuario: Usuario, autor: Autor): Boolean {
        return usuario.autoresPreferidos.contains(autor)
    }


    private fun existeValoracion(usuario: Usuario): Boolean {
        return valoraciones.any { it.usuario == usuario }
    }

    // VALIDACIONES
    private fun requerimientosDeUsuarioAmigo(usuario: Usuario): Boolean{
        return (creador.listaDeAmigos.contains(usuario) && usuario.leyoLibrosRecomendados(this.librosRecomendados))
    }

    private fun checkearPrivilegiosDeEdicion(usuario: Usuario): Boolean {
        return (usuario == creador || requerimientosDeUsuarioAmigo(usuario))
    }

    private fun checkearSiLeyo(usuario: Usuario, libro: Libro): Boolean{
        return if (usuario == creador){
            usuario.leyoElLibro(libro)
        } else {
            creador.leyoElLibro(libro) && usuario.leyoElLibro(libro)
        }
    }
    private fun validarPermiso(usuario: Usuario){
        require(checkearPrivilegiosDeEdicion(usuario))
    }

    // COMANDOS PARA LA EDICION DE LA RECOMENDACION
    fun revertirPrivacidad(usuario: Usuario){
        validarPermiso(usuario)
        publico = !publico
    }

    fun cambiarNombre(usuario: Usuario, nuevoNombre: String){
        validarPermiso(usuario)
        nombre = nuevoNombre
    }

    fun eliminarLibroRecomendado(usuario: Usuario, libro: Libro){
        validarPermiso(usuario)
        librosRecomendados.remove(libro)
    }

    fun agregarLibro(usuario: Usuario, libro: Libro){
        validarPermiso(usuario)
        require(checkearSiLeyo(usuario, libro))
        librosRecomendados.add(libro)
        creador.triggerUserAction(usuario,libro,this)
    }

    fun emitirDetalle(usuario:Usuario, nuevoDetalle: String){
        validarPermiso(usuario)
        detalle = nuevoDetalle
    }

    fun calcularTiempoTotalDeLectura(usuario: Usuario): Double{
        return librosRecomendados.sumOf { usuario.tiempoLecturaPromedio(it) }
    }

    fun clearContribuciones(){
        contribuciones.clear()
    }

}
