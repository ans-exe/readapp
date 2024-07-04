package AppDeLibros

interface RepositorioDatos {
    var id : Int
    fun searchCondition(texto: String): Boolean
}

open class Repositorio<T : RepositorioDatos> {

    val elementos: MutableList<T> = mutableListOf()
    private var idCounter = 1

    fun create(elemento : T){
        elementos.add(elemento)
        elemento.id = idCounter++
    }

    fun delete(elemento: T) {
        elementos.remove(elemento)
    }

    fun massiveDelete(list: List<T>){
        list.forEach{this.delete(it)}
    }

    fun agregarTodos(list: List<T>){
        list.forEach{this.create(it)}
    }

    fun update(elementoActualizado : T) {
        idDoesNotExist(elementoActualizado.id)
        val elementoAEliminar = getById(elementoActualizado.id)
        elementos.remove(elementoAEliminar)
        elementos.add(elementoActualizado)
    }

    fun getById(id: Int): T {
        idDoesNotExist(id)
        return elementos.first{it.id == id}
    }

    fun search(texto: String): List<T> {
        return elementos.filter {it.searchCondition(texto)}
    }

    private fun idExist(id: Int): Boolean {
        return elementos.any{it.id == id}
    }

    private fun idDoesNotExist(id: Int) {
        if(!idExist(id)){
            throw NotFoundException(ErrorMessages.ID_INEXISTENTE)
        }
    }
}

class UserRepository: Repositorio<Usuario>() {

    fun getInactives(): List<Usuario> {
        return elementos.filter {
            it.followers.isEmpty() && it.listaDeRecomendaciones.isEmpty()}
    }
}

class RecomendacionRepository : Repositorio<Recomendacion>() {

    fun buscarValoracionDeUsuario(usuario: Usuario): List<Recomendacion> {
        return elementos.filter { recomendacion ->
            recomendacion.valoraciones.any { valoracion ->
                valoracion.usuario == usuario
            }
        }
    }
}

class LibroRepository: Repositorio<Libro>()

class CentroRepository : Repositorio<CentroLectura>(){
    fun getInactives(): List<CentroLectura> {
        return elementos.filter { (it.expiro())}
    }

}

class AutorRepository: Repositorio<Autor>()
