import AppDeLibros.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProcesoTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest
    val admin = Admin()

    describe("Eliminar usuarios inactivos") {
        // ARRANGE
        val userRepository = UserRepository()

        it("debería eliminar solo usuarios inactivos") {
            // ARRANGE

            val usuarioActivo1 = crearUsuario()
            val usuarioActivo2 = crearUsuario()
            val usuarioInactivo = crearUsuario()
            usuarioActivo2.agregarAmigo(usuarioActivo1)
            usuarioActivo1.agregarAmigo(usuarioActivo2)
            userRepository.agregarTodos(listOf(usuarioInactivo, usuarioActivo1, usuarioActivo2))


            admin.addProcess(DeleteInactiveUser(userRepository, StubMailSender))
            admin.run()

            // ASSERT
            val usuariosRestantes = userRepository.elementos
            usuariosRestantes.shouldHaveSize(2)

        }
        it("debería enviar un correo electrónico al finalizar el proceso") {
            // ARRANGE

            userRepository.elementos.add(crearUsuario())

            // ACT
            StubMailSender.reset()
            admin.addProcess(DeleteInactiveUser(userRepository, StubMailSender))
            admin.run()

            // ASSERT
            StubMailSender.mailsSent.size shouldBe 1
            StubMailSender.mailsSent[0].subject shouldBe "Se realizó el proceso: DeleteInactiveUser"
        }
    }
    describe("Actualización de Libros") {

        it("debería actualizar los libros del repositorio correctamente") {
            // ARRANGE
            val libro = crearLibro(titulo = "Nuevo Libro", cantidadPalabras = 100, cantidadPaginas = 10, cantidadEdiciones = 1, ventasSemanales = 0, autor = crearAutor())

            // ACT
            UpdaterLibros.serviceLibros = object : IServiceLibros {
                override fun getLibros(): String {
                    return """[{"titulo":"Nuevo Libro","cantidadPalabras":100,"cantidadPaginas":10,"cantidadEdiciones":1,"ventasSemanales":0}]"""
                }
            }
            UpdaterLibros.repositorio = Repositorio()
            UpdaterLibros.repositorio.elementos.add(libro)

            admin.addProcess(ActualizacionLibros(UpdaterLibros, StubMailSender))
            admin.run()

            // ASSERT
            UpdaterLibros.repositorio.elementos.size shouldBe 1
            UpdaterLibros.repositorio.elementos[0].titulo shouldBe "Nuevo Libro"
        }

        it("debería enviar un correo electrónico al finalizar el proceso") {
            // ACT
            UpdaterLibros.serviceLibros = object : IServiceLibros {
                override fun getLibros(): String {
                    return "[]"
                }
            }
            UpdaterLibros.repositorio = Repositorio()
            StubMailSender.reset()
            admin.addProcess(ActualizacionLibros(UpdaterLibros, StubMailSender))
            admin.run()

            // ASSERT
            StubMailSender.mailsSent.size shouldBe 1
            StubMailSender.mailsSent[0].subject shouldBe "Se realizó el proceso: ActualizacionLibros"
        }
    }

    describe("Borrar Centros de Lecturas expirados") {

        it("debería borrar los centros de lectura expirados correctamente") {
            // ARRANGE
            val centro1 = CentroLecturaMock(true, 0)
            val centro2 = CentroLecturaMock(false, 0)
            val centroRepository = CentroRepository()
            centroRepository.agregarTodos(listOf(centro1, centro2))

            // ACT
            admin.addProcess(BorrarCentrosLecturaExpirados(centroRepository, StubMailSender))
            admin.run()

            // ASSERT
            centroRepository.elementos.size shouldBe 1
            centroRepository.elementos[0] shouldBe centro2
        }

        it("debería enviar un correo electrónico al finalizar el proceso") {
            // ARRANGE
            val centroRepository = CentroRepository()
            val centro = CentroLecturaMock(true, 0)
            centroRepository.create(centro)


            // ACT
            StubMailSender.reset()
            admin.addProcess(BorrarCentrosLecturaExpirados(centroRepository, StubMailSender))
            admin.run()


            // ASSERT
            StubMailSender.mailsSent.size shouldBe 1
            StubMailSender.mailsSent[0].subject shouldBe "Se realizó el proceso: BorrarCentrosLecturaExpirados"
        }
    }

    describe("Agregar autores de forma masiva") {
        // ARRANGE
        val autorRepository = AutorRepository()
        val autores = listOf(
            crearAutor("J.K. Rowling"),
            crearAutor("Stephen King"),
            crearAutor("Gabriel García Márquez")
        )

        it("debería agregar todos los autores de forma masiva") {
            // ACT
            autorRepository.agregarTodos(autores)

            // ASSERT
            val autoresEnRepository = autorRepository.elementos
            autoresEnRepository.shouldContainAll(autores)
        }

        it("debería enviar un correo electrónico al finalizar el proceso de agregar autores") {
            // ARRANGE
            val autorRepository = AutorRepository()
            val autores = mutableListOf(
                crearAutor("Autor1"),
                crearAutor("Autor2"),

                )
            autorRepository.agregarTodos(autores)
            StubMailSender.reset()

            // ACT
            admin.addProcess(AgregarAutores(autorRepository,autores, StubMailSender))
            admin.run()

            // ASSERT
            StubMailSender.mailsSent.size shouldBe 1
            StubMailSender.mailsSent[0].subject shouldBe "Se realizó el proceso: AgregarAutores"
        }


    }


})

//Clase de prueba mock para simular los centros de lectura
class CentroLecturaMock(private val expirado: Boolean, override var id: Int) : CentroLectura("", "", 0, emptyList(), 0) {
    override fun calcularCostoReserva(): Double = 0.0
    override fun calcularMaximoParticipantes(): Int = 0
    override fun reservaDisponible(): Boolean = false
    override fun searchCondition(texto: String): Boolean = false

    override fun expiro(): Boolean = expirado
}