package AppDeLibros

class Valoracion(
    val usuario: Usuario,
    var valor: Double,
    var comentario: String
) {
    fun editar(nuevoValor: Double, nuevoComentario: String) {
        valor = nuevoValor
        comentario = nuevoComentario
    }
}
