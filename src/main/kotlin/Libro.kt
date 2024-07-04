package AppDeLibros

data class Libro(
    var titulo: String,
    val cantidadPalabras: Int,
    val cantidadPaginas: Int,
    val paginasParaSerLargo: Int = 600,
    val cantidadEdiciones: Int,
    val ventasSemanales: Int,
    val autor: Autor,
    val lenguajesTraduccion: List<Lenguaje>,
    var lenguajesPublicados: List<Lenguaje> = lenguajesTraduccion + autor.idiomaNativo,
    val lecturaCompleja: Boolean,
):RepositorioDatos{

    override var id: Int = 0

    override fun searchCondition(texto: String): Boolean {
        return coincideTitulo(texto) || coincideApellidoAutor(texto)
    }
    fun coincideTitulo(texto: String): Boolean {
        return (titulo.contains(texto, ignoreCase = true))
    }

    fun coincideApellidoAutor(texto: String): Boolean{
        return autor.apellido.equals(texto, ignoreCase = true)
    }

    fun esLargo(): Boolean{
        return cantidadPaginas > paginasParaSerLargo
    }

    fun esDesafiante(): Boolean {
        return lecturaCompleja || esLargo()
    }

    private fun esVariado(): Boolean{
        return (cantidadEdiciones > 2 || lenguajesTraduccion.size >= 5)
    }

    fun esBestSeller(): Boolean {
        return ventasSemanales >= 10000 && esVariado()
    }
}