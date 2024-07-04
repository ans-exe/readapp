import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import AppDeLibros.*

//Tiempo de lectura segun el tipo de lector (puede variar segun libro y forma en que leen)}

class TipoLectorTesting : DescribeSpec({

    describe("Test de Lector Ansioso") {
        it("Reduce el tiempo de lectura en un 20%") {
            // Arrange
            val usuario = crearUsuario(palabrasPorMinuto = 200)
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 48, false, "")
            val libro = crearLibro(autor = autor)

            // Act
            val tiempoNormal = usuario.tipoLector.calcularTiempo(usuario, libro)
            usuario.tipoLector = Ansioso()
            val tiempoAnsioso = usuario.tipoLector.calcularTiempo(usuario, libro)

            // Assert
            tiempoAnsioso shouldBe (tiempoNormal * 0.8)
        }


        it("Reduce el tiempo de lectura a la mitad para un best seller") {
            // Arrange
            val usuario = crearUsuario(palabrasPorMinuto = 200)
            val autor = Autor("Ramon", "Diaz", Lenguaje.ESPANOL, 48, false, "")
            val libro = crearLibro(autor = autor, ventasSemanales = 10000)

            // Act
            usuario.tipoLector = Ansioso()
            val tiempoAnsioso = usuario.tipoLector.calcularTiempo(usuario, libro)

            // Assert
            tiempoAnsioso shouldBe (usuario.tiempoLecturaPromedio(libro) * 0.5)
        }
    }

    describe("Test de Lector Fanatico") {
        it("Aumenta el tiempo de lectura para un autor preferido") {
            // Arrange
            val autor1 = Autor("Luis", "Lopez", Lenguaje.ESPANOL, 48, false, "")
            val usuario = crearUsuario(palabrasPorMinuto = 100, tipoLector = Fanatico(), autoresPreferidos = mutableListOf(autor1))
            val libro = crearLibro(autor = autor1, cantidadPaginas = 500, cantidadPalabras = 1000)

            // Act
            val tiempoFanatico = usuario.tipoLector.calcularTiempo(usuario, libro)
            val tiempoEsperado = 1010

            // Assert
            tiempoFanatico shouldBe tiempoEsperado
        }

        it("Aumenta el tiempo de lectura para un autor preferido y largo") {
            // Arrange
            val autor1 = Autor("Luis", "Lopez", Lenguaje.ESPANOL, 48, false, "")
            val autor2 = crearAutor()
            val usuario = crearUsuario(palabrasPorMinuto = 100, tipoLector = Fanatico())
            val libro = crearLibro(autor = autor1, cantidadPaginas = 650, cantidadPalabras = 1000)

            // Act
            usuario.autoresPreferidos.add(autor1)
            val tiempoFanatico = usuario.tipoLector.calcularTiempo(usuario, libro)

            // Assert
            val tiempoEsperado = 1200 + 50 + 20

            tiempoFanatico shouldBe tiempoEsperado
        }
    }

    describe("Test de Lector Recurrente") {

        it("Disminuye la velocidad de lectura en un 1% por cada lectura adicional del mismo libro") {
            // Arrange
            val autor1 = crearAutor()
            val usuario1 = crearUsuario(palabrasPorMinuto = 100, tipoLector = Recurrente())
            val libro1 = crearLibro(autor = autor1, cantidadPalabras = 1000, cantidadPaginas = 500)

            // Act
            usuario1.leerLibro(libro1)
            usuario1.leerLibro(libro1)
            usuario1.leerLibro(libro1)
            usuario1.leerLibro(libro1)
            usuario1.leerLibro(libro1)
            usuario1.leerLibro(libro1)

            //val tiempoEsperado = 9.6

            // Assert
            usuario1.vecesLeido(libro1) shouldBe 6

        }
    }
})