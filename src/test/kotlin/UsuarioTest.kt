import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import AppDeLibros.*
import AppDeLibros.Recomendacion
import nonapi.io.github.classgraph.json.Id

fun crearUsuario(
    nombre: String = "",
    apellido: String = "",
    username: String = "",
    correo: String = "",
    fechaNacimiento: LocalDate? = null,
    palabrasPorMinuto: Int = 0,
    listaDeAmigos: MutableList<Usuario> = mutableListOf(),
    librosLeidos: MutableMap<Libro, Int> = mutableMapOf(),
    librosALeer: MutableList<Libro> = mutableListOf(),
    listaDeRecomendaciones: MutableList<Recomendacion> = mutableListOf(),
    recomendacionesAValorar: MutableList<Recomendacion> = mutableListOf(),
    autoresPreferidos: MutableList<Autor> = mutableListOf(),
    tipoLector: TipoLector = LectorPromedio(),
    tipoPerfil : IPerfil = Leedor(),
    idiomaNativo: Lenguaje = Lenguaje.ESPANOL,
    rangoMinimoLectura: Double = 0.0,
    rangoMaximoLectura: Double = 10.0,
    followers: MutableList<Usuario> = mutableListOf()

    ): Usuario {
    return Usuario(
        nombre, apellido, username, correo, fechaNacimiento ?: LocalDate.now(),
        palabrasPorMinuto, listaDeAmigos,
        librosLeidos, librosALeer, listaDeRecomendaciones, recomendacionesAValorar, autoresPreferidos,
        tipoLector, tipoPerfil, idiomaNativo, rangoMinimoLectura, rangoMaximoLectura, followers
    )
}


class UsuarioTesting : DescribeSpec({

    describe("Tests de Años") {

        it("Si el usuario nacio el 1 de enero de 2000, tiene 24 años") {
            // Arrange
            val unUsuario = crearUsuario(fechaNacimiento = LocalDate.of(2000, 1, 1))

            // Act
            val edadActual = LocalDate.now().year - unUsuario.fechaNacimiento.year

            // Assert
            edadActual shouldBe 24
        }
    }
})