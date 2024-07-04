package AppDeLibros
import java.time.LocalDate
import java.time.Period

data class Usuario(
    val nombre: String,
    val apellido: String,
    var username: String,
    val correo: String,
    val fechaNacimiento: LocalDate,
    val palabrasPorMinuto: Int,
    val listaDeAmigos: MutableList<Usuario>,                 // Este Usuario considera Otros Usuarios
    val librosLeidos: MutableMap<Libro, Int> = mutableMapOf(),
    val librosALeer: MutableList<Libro>,
    val listaDeRecomendaciones: MutableList<Recomendacion>,
    val recomendacionesAValorar: MutableList<Recomendacion>,
    val autoresPreferidos: MutableList<Autor>,
    var tipoLector: TipoLector,
    var tipoPerfil: IPerfil,
    val idiomaNativo: Lenguaje,
    val rangoMinimoLectura: Double,
    val rangoMaximoLectura: Double,
    val followers: MutableList<Usuario>,        // Otros Usuarios consideran a este Usuario
):RepositorioDatos{


    override var id: Int = 0

    override fun searchCondition(texto: String): Boolean {
        return coincideNombre(texto) || coincideUsername(texto)
    }
    private fun coincideNombre(texto: String): Boolean {
       return (nombre.contains(texto, ignoreCase = true) || apellido.contains(texto, ignoreCase = true))
    }

    private fun coincideUsername(texto: String): Boolean{
       return username.equals(texto, ignoreCase = true)
    }

    val edad: Int
        get() {
            val hoy = LocalDate.now()
            val periodo = Period.between(fechaNacimiento, hoy)
            return periodo.years
        }

    // DEVUELVE LO QUE TARDA EN LEER UN LIBRO EN MINUTOS
    // SI ES DESAFIANTE TARDA EL DOBLE
    fun tiempoLecturaPromedio(libro: Libro): Double{
        val factorDesafiante = if (libro.esDesafiante()) 2 else 1
        return ((libro.cantidadPalabras.toFloat() / palabrasPorMinuto) * factorDesafiante).toDouble()
    }

    fun leerLibro(libro: Libro){
        librosLeidos[libro] = (librosLeidos[libro] ?: 0) + 1
    }

    fun vecesLeido(libro: Libro): Int = librosLeidos[libro] ?:0

    fun leyoElLibro(libro: Libro): Boolean{
        return librosLeidos.containsKey(libro)
    }

    fun leyoLibrosRecomendados(librosRecomendados: List<Libro>): Boolean{
        return (librosLeidos.keys.containsAll(librosRecomendados))
    }

    fun libroALeer(libro: Libro) {
        if (leyoElLibro(libro)) {
            throw IllegalStateException("Error: El libro '${libro.autor}' ya fue leido.")
        } else {
            librosALeer.add(libro)
        }
    }

    fun agregarAmigo(usuario: Usuario){
        listaDeAmigos.add(usuario)
        usuario.agregarFollower(this)
    }

    fun agregarFollower(usuario: Usuario){
        followers.add(usuario)
    }

    fun crearUnaRecomendacion(recomendacion: Recomendacion){
        listaDeRecomendaciones.add(recomendacion)
    }


    //------------------------- ACOPLAMIENTO DE ACCIONES -------------------------//
    private val accionesDeUsuarioActivas: MutableSet<AccionesUsuario> = mutableSetOf()

    fun addAction(accion: AccionesUsuario) = accionesDeUsuarioActivas.add(accion)

    fun removeAction(accion: AccionesUsuario) = accionesDeUsuarioActivas.remove(accion)

    fun clearActions() = accionesDeUsuarioActivas.clear()

    fun triggerUserAction(usuario: Usuario, libro: Libro, recomendacion: Recomendacion){
        accionesDeUsuarioActivas.forEach { it.ejecutarAccion(usuario, libro, recomendacion)}
    }

}
