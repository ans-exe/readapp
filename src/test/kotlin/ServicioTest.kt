import AppDeLibros.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ServicioTest: DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    describe("Actualizacion de Libros"){

        val autor = crearAutor()
        var libro1 = crearLibro(autor = autor, ventasSemanales = 10, cantidadEdiciones = 10)
        var libro2 = crearLibro(autor = autor, ventasSemanales = 20, cantidadEdiciones = 20)
        var libro3 = crearLibro(autor = autor, ventasSemanales = 30, cantidadEdiciones = 30)
        var libro4 = crearLibro(autor = autor, ventasSemanales = 40, cantidadEdiciones = 40)

        val repositorioDeLibros = Repositorio<Libro>()
        repositorioDeLibros.apply { create(libro1);create(libro2);create(libro3);create(libro4); }

        UpdaterLibros.serviceLibros = StubServiceLibro()
        UpdaterLibros.repositorio = repositorioDeLibros

        it("Al actualizar los libros, los elementos cambian") {
            // Arrange

            // Act
            UpdaterLibros.updateLibros()

            // Assert
            val libroActualizado1 = repositorioDeLibros.getById(1)
            libroActualizado1.cantidadEdiciones shouldBe 2
            libroActualizado1.ventasSemanales shouldBe 2000

            val libroActualizado2 = repositorioDeLibros.getById(2)
            libroActualizado2.cantidadEdiciones shouldBe 3
            libroActualizado2.ventasSemanales shouldBe 11000
        }

        it("Si no hay JSON para actualizar, el libro no se actualiza") {
            // Act
            UpdaterLibros.updateLibros()

            // Assert

            val libroSinActualizar = repositorioDeLibros.getById(4)
            libroSinActualizar.cantidadEdiciones shouldBe 40
            libroSinActualizar.ventasSemanales shouldBe 40
        }
    }
})

class StubServiceLibro : IServiceLibros{
    private val librosJSON = """
        [
//         {
//           "id": 5,
//           "ediciones": 6,
//           "ventasSemanales": 15000
//         },
//         {
//           "id": 12,
//           "ediciones": 1,
//           "ventasSemanales": 1000
//         },
         {
           "id": 2,
           "ediciones": 3,
           "ventasSemanales": 11000
         },
         {
           "id": 1,
           "ediciones": 2,
           "ventasSemanales": 2000
         }
        ]
    """.trimIndent()

    override fun getLibros(): String {
        return librosJSON
    }
}
