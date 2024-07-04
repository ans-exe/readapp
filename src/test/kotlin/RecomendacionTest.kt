import AppDeLibros.*
import AppDeLibros.Recomendacion
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.*

fun crearRecomendacion(
    nombre: String = "",
    publico: Boolean = true,
    creador: Usuario,
    librosRecomendados: MutableList<Libro> = mutableListOf(),
    detalle: String = "",
    contribuciones: MutableMap<Usuario,MutableList<Libro>> = mutableMapOf(),
    contribucionesLimite: Int = 10
) : Recomendacion {
    return Recomendacion(
        nombre, publico, creador, librosRecomendados, detalle, contribuciones, contribucionesLimite)
}

class RecomendacionTesting : DescribeSpec({

        describe("Test de Edicion de Privacidad") {

            it("Creador revierte la privacidad") {
                // Arrange
                val usuario1 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1, publico = true)

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)
                recomendacion1.revertirPrivacidad(usuario1)

                // Assert
                recomendacion1.publico shouldBe false
            }

            it ("Amigo que cumple requisitos revierte de privacidad") {
                // Arrange
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario()
                val autor = crearAutor()
                val libro1 = crearLibro(autor = autor)
                val libro2 = crearLibro(autor = autor)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1, publico = true, librosRecomendados = mutableListOf(libro2,libro2)
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)
                usuario1.agregarAmigo(usuario2)

                usuario2.leerLibro(libro1)
                usuario2.leerLibro(libro2)

                recomendacion1.revertirPrivacidad(usuario2)

                // Assert
                recomendacion1.publico shouldBe false
            }

            it("Amigo que NO cumple requisitos revierte privacidad") {
                // Arrange
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario()
                val autor = crearAutor()
                val libro1 = crearLibro(autor = autor)
                val libro2 = crearLibro(autor = autor)
                val recomendacion1 = crearRecomendacion(creador = usuario1, publico = true)

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                val exception = shouldThrow<IllegalArgumentException> {
                recomendacion1.revertirPrivacidad(usuario2)
                }

                // Assert
                exception.message shouldBe "Failed requirement." // Mensaje de error del 'require'
                recomendacion1.publico shouldBe true // La privacidad no debería cambiar
            }
        }

        describe("Test de Edicion de Cambiar Nombre"){

            it("Creador cambia el nombre") {
                // Arrange
                val usuario1 = crearUsuario()
                val recomendacion1 = crearRecomendacion(creador = usuario1)

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                recomendacion1.cambiarNombre(usuario1, "Nuevo nombre")

                // Assert
                recomendacion1.nombre shouldBe "Nuevo nombre"
            }

            it("Amigo que NO cumple requisitos cambia el nombre") {
                // Arrange
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1, librosRecomendados = mutableListOf(libro1)
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)
                usuario1.agregarAmigo(usuario2)

                val exception = shouldThrow<IllegalArgumentException> {
                    recomendacion1.cambiarNombre(usuario2, "Nuevo nombre")
                }

                // Assert
                exception.message shouldBe "Failed requirement." // Mensaje de error del 'require'
                recomendacion1.nombre shouldBe "" // No cambia el nombre
            }
        }

        //describe("Test de Eliminar libro Recomendado")

        describe("Test de Agregar Libro a Recomendacion"){

            it("Creador agrega libros que leyo"){
                // Arrange
                val usuario1 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val libro2 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                usuario1.leerLibro(libro1)
                usuario1.leerLibro(libro2)

                recomendacion1.agregarLibro(usuario1,libro1)
                recomendacion1.agregarLibro(usuario1,libro2)

                // Assert
                // La documentacion no es clara, hermano.
                recomendacion1.librosRecomendados shouldContainAll listOf(libro1,libro2)
            }

            it("Creador agrega libro que NO leyo"){
                // Arrange
                val usuario1 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val libro2 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                usuario1.leerLibro(libro1)

                recomendacion1.agregarLibro(usuario1,libro1)
                recomendacion1.agregarLibro(usuario1,libro2)

                // Assert
                // La documentacion no es clara, hermano.
                recomendacion1.librosRecomendados shouldContainAll listOf(libro1)
            }

            it("Amigo agrega libros que ambos (Creador y Amigo) leyeron"){
                // Arrange
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val libro2 = crearLibro(autor = autor1)
                val libro3 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                usuario1.leerLibro(libro1)
                usuario1.leerLibro(libro2)
                usuario1.leerLibro(libro3)

                usuario1.agregarAmigo(usuario2)

                usuario2.leerLibro(libro1)
                usuario2.leerLibro(libro2)
                usuario2.leerLibro(libro3)

                recomendacion1.agregarLibro(usuario2,libro1)
                recomendacion1.agregarLibro(usuario2,libro2)
                recomendacion1.agregarLibro(usuario2,libro3)

                // Assert
                // La documentacion no es clara, hermano.
                recomendacion1.librosRecomendados shouldContainAll listOf(libro1,libro2,libro3)
            }

            it("Amigo agrega libro que él leyo pero NO el creador"){
                // Arrange
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario()
                val autor1 = crearAutor()
                val libro1 = crearLibro(autor = autor1)
                val libro2 = crearLibro(autor = autor1)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1
                )

                // Act
                usuario1.crearUnaRecomendacion(recomendacion1)

                usuario1.agregarAmigo(usuario2)

                usuario1.leerLibro(libro1)

                usuario2.leerLibro(libro1)
                usuario2.leerLibro(libro2)

                recomendacion1.agregarLibro(usuario2,libro1)
                recomendacion1.agregarLibro(usuario2,libro2)

                // Assert
                recomendacion1.librosRecomendados.shouldContainAll(libro1)
            }
        }

        //describe("Test de Reseñas/Detalles")

        describe("Tiempo de Lectura de un Usuario"){

            it("Cuanto le lleva leer todos los libros a un Usuario"){
                // Arrange
                // ACLARACION: PRIMERO TESTEAMOS SIN TIPO DE LECTORES
                val usuario1 = crearUsuario()
                val usuario2 = crearUsuario(palabrasPorMinuto = 50)
                val autor1 = crearAutor()
                // EL USUARIO 2 TARDARIA 20 MINUTOS EN LEER EL LIBRO 1
                val libro1 = crearLibro(
                    autor = autor1, cantidadPalabras = 1000)
                // EL USUARIO 2 TARDARIA 40 MINUTOS EN LEER EL LIBRO 1
                val libro2 = crearLibro(
                    autor = autor1, cantidadPalabras = 1000, lecturaCompleja = true, cantidadPaginas = 650)
                val recomendacion1 = crearRecomendacion(
                    creador = usuario1
                )

                // Act
                usuario1.agregarAmigo(usuario2)

                usuario1.crearUnaRecomendacion(recomendacion1)

                usuario1.leerLibro(libro1)
                usuario1.leerLibro(libro2)

                recomendacion1.agregarLibro(usuario1,libro1)
                recomendacion1.agregarLibro(usuario1,libro2)

                val tiempoTotal: Double = recomendacion1.calcularTiempoTotalDeLectura(usuario2)

                // Assert
                recomendacion1.librosRecomendados shouldContainAll listOf(libro2)
                tiempoTotal shouldBe 60

            }
        }
})
