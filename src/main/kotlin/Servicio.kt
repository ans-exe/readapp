package AppDeLibros

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

interface IServiceLibros {
    fun getLibros(): String
}

object UpdaterLibros{
    lateinit var serviceLibros: IServiceLibros
    lateinit var repositorio: Repositorio<Libro>

    private fun fromJsonToListLibro(): List<Libro> {
        val gson = Gson()
        val json = serviceLibros.getLibros()
        val modifiedJson = modifyJson(json)
        val tipoListaLibros: Type = object : TypeToken<List<Libro?>?>() {}.type
        return gson.fromJson(modifiedJson, tipoListaLibros)
    }

    fun updateLibros() {
        fromJsonToListLibro().forEach{(repositorio.update(it))}
    }

    private fun modifyJson(json: String): String {
        val modifiedJson = json.replace("ediciones", "cantidadEdiciones")
        return modifiedJson
    }
}
