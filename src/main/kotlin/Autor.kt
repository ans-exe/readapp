package AppDeLibros

class Autor (
    val nombre: String,
    val apellido: String,
    val idiomaNativo: Lenguaje,
    val edad : Int,
    val ganoPremios: Boolean,
    val seudonimo: String,
):RepositorioDatos{

    override var id: Int = 0

    override fun searchCondition(texto: String): Boolean {
        return coincideNombre(texto) || coincideSeudonimo(texto)
    }
    private fun coincideNombre(texto: String): Boolean {
        return (nombre.contains(texto, ignoreCase = true) || apellido.contains(texto, ignoreCase = true))
    }
    private fun coincideSeudonimo(texto: String): Boolean{
        return seudonimo.equals(texto, ignoreCase = true)
    }

    private fun suficienteEdad(): Boolean {
        return edad > 50
    }

    fun esConsagrado(): Boolean {
        return ganoPremios && suficienteEdad()
    }
}
