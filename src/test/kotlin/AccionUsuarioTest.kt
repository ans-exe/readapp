import AppDeLibros.*
import io.kotest.assertions.throwables.shouldThrow

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe

class AccionUsuarioTesting : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    // ARRANGE
    val autor = crearAutor()

    val libro1 = crearLibro("Titulo1",autor = autor)
    val libro2 = crearLibro("Titulo2",autor = autor)
    val libro3 = crearLibro("Titulo3",autor = autor)
    val libro4 = crearLibro("Titulo4",autor = autor)

    val listaDeLibrosLeidos = mutableMapOf(libro1 to 1,libro2 to 1, libro3 to 1, libro4 to 1)
    val listaDeLibros = mutableListOf(libro1)

    val usuarioAmigo = crearUsuario(
        username = "User2", librosLeidos = listaDeLibrosLeidos)

    val usuarioCreador = crearUsuario(
        username = "User1", librosLeidos = listaDeLibrosLeidos,
        listaDeAmigos = mutableListOf(usuarioAmigo)
    )

    val recomendacion1 = crearRecomendacion(
        creador = usuarioCreador, librosRecomendados = (listaDeLibros), contribucionesLimite = 2)

    describe("Llevar un registro por recomendacion") {

        it("Registrar correctamente las contribuciones sin descriminar al creador") {

            // ACT
            recomendacion1.clearContribuciones()
            usuarioCreador.addAction(RegistrarContribuciones())

            recomendacion1.agregarLibro(usuarioAmigo,libro2)
            recomendacion1.agregarLibro(usuarioAmigo,libro3)
            recomendacion1.agregarLibro(usuarioCreador,libro4)

            // ASSERT
            recomendacion1.contribuciones[usuarioAmigo].shouldContainExactly(libro2,libro3)
            recomendacion1.contribuciones[usuarioCreador].shouldContainExactly(libro4)
        }

        it("Si la accion de registro no esta activa, no hay que registrar.") {
            // ACT
            recomendacion1.clearContribuciones()
            usuarioCreador.clearActions()

            recomendacion1.agregarLibro(usuarioAmigo,libro2)
            recomendacion1.agregarLibro(usuarioAmigo,libro3)
            recomendacion1.agregarLibro(usuarioCreador,libro4)

            // ASSERT
            recomendacion1.contribuciones.shouldBeEmpty()
        }

        it("Usuario puede agregar libros si NO se pasa del limite.") {
            // ACT
            recomendacion1.clearContribuciones()
            usuarioCreador.clearActions()
            usuarioCreador.addAction(RegistrarContribuciones())
            usuarioCreador.addAction(ControlDeContribuciones())

            recomendacion1.agregarLibro(usuarioAmigo,libro2)

            // ASSERT
            recomendacion1.contribuciones[usuarioAmigo].shouldContainExactly(libro2)
            recomendacion1.creador.listaDeAmigos shouldContain (usuarioAmigo)
        }

        it("Usuario NO puede agregar libros si se pasa del limite.") {
            // ACT
            recomendacion1.clearContribuciones()
            usuarioCreador.clearActions()

            usuarioCreador.addAction(RegistrarContribuciones())
            usuarioCreador.addAction(ControlDeContribuciones())

            recomendacion1.agregarLibro(usuarioAmigo,libro2)
            recomendacion1.agregarLibro(usuarioAmigo,libro3)

            val exception = shouldThrow<IllegalArgumentException> {
                recomendacion1.agregarLibro(usuarioAmigo, libro4)
            }

            // ASSERT
            exception.message shouldBe "Failed requirement." // Mensaje de error del 'require'
            recomendacion1.contribuciones[usuarioAmigo].shouldContainExactly(libro2,libro3)
            recomendacion1.creador.listaDeAmigos.shouldBeEmpty()
        }
    }
})