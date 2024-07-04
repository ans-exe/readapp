import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import AppDeLibros.*

fun crearLibro(
    titulo: String = "",
    cantidadPalabras: Int = 0,
    cantidadPaginas: Int = 0,
    paginasParaSerLargo: Int = 600,
    cantidadEdiciones: Int = 0,
    ventasSemanales: Int = 0,
    autor: Autor,
    lenguajesTraduccion: List<Lenguaje> = listOf(),
    lenguajesPublicados: List<Lenguaje>? = null,
    lecturaCompleja: Boolean = false,
): Libro {
    val lenguajesPub = lenguajesPublicados ?: autor.idiomaNativo.let { listOf(it) + lenguajesTraduccion }
    return Libro(
        titulo, cantidadPalabras, cantidadPaginas, paginasParaSerLargo, cantidadEdiciones,
        ventasSemanales, autor, lenguajesTraduccion, lenguajesPub, lecturaCompleja
    )
}

class LibroTesting : DescribeSpec({

    describe("Test de Desafiante"){

        it("Si es desafiante (por ser complejo), devuelve true") {
            // Arrange
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 50, true, "" )
            val libro = Libro(
                titulo = "A la B",
                cantidadPalabras = 50000,
                cantidadPaginas = 400,
                cantidadEdiciones = 3,
                ventasSemanales = 12000,
                lenguajesTraduccion = listOf(Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES),
                lecturaCompleja = true,
                autor = autor,

            )

            // Act & Assert
            libro.esDesafiante() shouldBe true
        }

        it("Si NO es desafiante (ni largo, ni complejo), devuelve false") {
            // Arrange
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 48, false, "")
            val libro = Libro(
                titulo = "A la B",
                cantidadPalabras = 50000,
                cantidadPaginas = 400,
                cantidadEdiciones = 3,
                ventasSemanales = 12000,
                lenguajesTraduccion = listOf(Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES),
                lecturaCompleja = false,
                autor = autor,
            )

            // Act & Assert
            libro.esDesafiante() shouldBe false
        }
    }

    describe("Test de Best Seller"){

        it("Si vende mas de 10000 y es variado, es Best Seller, devuelve true") {
            // Arrange
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 22, true, "")
            val libro = Libro(
                titulo = "A la B",
                cantidadPalabras = 50000,
                cantidadPaginas = 400,
                cantidadEdiciones = 3,
                ventasSemanales = 12000,
                lenguajesTraduccion = listOf(Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES),
                lecturaCompleja = false,
                autor = autor,

            )

            // Act & Assert
            libro.esBestSeller() shouldBe true
        }

        it("Si NO vende mas de 10000 y es variado, es Best Seller, devuelve false") {
            // Arrange
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 18, true, "")
            val libro = Libro(
                titulo = "A la B",
                cantidadPalabras = 50000,
                cantidadPaginas = 400,
                cantidadEdiciones = 3,
                ventasSemanales = 9999,
                lenguajesTraduccion = listOf(Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES),
                lecturaCompleja = false,
                autor = autor,

            )

            // Act & Assert
            libro.esBestSeller() shouldBe false
        }
    }

    describe("Lista de Lenguajes Publicados"){

        it("El libro esta escrito en español y traducido en aleman, ingles y frances") {
            // Arrange
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 65, false, "")
            val libro = Libro(
                titulo = "A la B",
                cantidadPalabras = 50000,
                cantidadPaginas = 400,
                cantidadEdiciones = 3,
                ventasSemanales = 12000,
                lenguajesTraduccion = listOf(Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES),
                lecturaCompleja = false,
                autor = autor,

            )

            // Act & Assert
            val lenguajesEsperados = listOf(Lenguaje.ESPANOL, Lenguaje.ALEMAN, Lenguaje.INGLES, Lenguaje.FRANCES)
            libro.lenguajesPublicados shouldContainExactlyInAnyOrder  lenguajesEsperados
        }
    }
})
