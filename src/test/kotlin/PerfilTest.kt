import AppDeLibros.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate

class PerfilLeedorTesting : DescribeSpec ({

    describe ("Testeo perfil leedor")
    {
        it("Le interesan todas las recomendaciones") {
            // Arrange
            val autor = crearAutor()
            val leedor = Leedor()
            val usuario = crearUsuario()
            val libro1 = crearLibro(autor = autor)
            val libro2 = crearLibro(autor = autor)
            val libro3 = crearLibro(autor = autor)
            val libro4 = crearLibro(autor = autor)
            val recomendacion1 = crearRecomendacion("Mi recomendación 1", publico = true, usuario, mutableListOf(libro1, libro2), "Esta es una excelente recomendación")
            val recomendacion2 = crearRecomendacion("Mi recomendación 2", publico = true, usuario, mutableListOf(libro1, libro3), "Esta es una excelente recomendación")
            val recomendacion3 = crearRecomendacion("Mi recomendación 3", publico = true, usuario, mutableListOf(libro2, libro4), "Esta es una excelente recomendación")
            val listaDeRecomendacion = (listOf(recomendacion1, recomendacion2, recomendacion3))

            // Act
            val resultado = leedor.buscarRecomendacion(listaDeRecomendacion, usuario)

            // Assert
            assertEquals(listaDeRecomendacion, resultado)
        }

    }

})

class PoliglotaTesting : DescribeSpec({

    describe("Testeo perfil Poliglota") {
        it("Debe devolver solo recomendaciones con al menos 5 idiomas distintos") {
            // Arrange
            val autor1 = Autor("Autor1", "Apellido1", Lenguaje.BENGALI, 30, true, "")
            val autor2 = Autor("Autor2", "Apellido2", Lenguaje.BENGALI, 40, true, "")
            val autor3 = Autor("Autor3", "Apellido3", Lenguaje.FRANCES, 50, true, "")

            val usuario = crearUsuario()

            val libro1 = crearLibro("Libro1", 500, autor = autor1,lenguajesPublicados = listOf(Lenguaje.ESPANOL))
            val libro2 = crearLibro("Libro2", 300, autor = autor2, lenguajesPublicados = mutableListOf(Lenguaje.INGLES, Lenguaje.FRANCES))
            val libro3 = crearLibro("Libro3", 250, autor = autor3, lenguajesPublicados = mutableListOf(Lenguaje.ESPANOL, Lenguaje.INGLES, Lenguaje.FRANCES))
            val libro4 = crearLibro("Libro4", 220, autor = autor2, lenguajesPublicados = mutableListOf(Lenguaje.ESPANOL, Lenguaje.INGLES, Lenguaje.FRANCES, Lenguaje.PORTUGUES, Lenguaje.ARABE))

            val recomendacion1 = crearRecomendacion("Mi recomendación 2", publico = true, usuario, mutableListOf(libro1,libro2), "Esta es una excelente recomendación")
            val recomendacion2 = crearRecomendacion("Mi recomendación 2", publico = true, usuario, mutableListOf(libro1,libro3), "Esta es una excelente recomendación")
            val recomendacion3 = crearRecomendacion("Mi recomendación 3", publico = true, usuario, mutableListOf(libro2,libro4), "Esta es una excelente recomendación")

            val listaRecomendaciones = listOf(recomendacion1, recomendacion2, recomendacion3)
            val perfilPoliglota = Poliglota()

            // Act
            val resultado = perfilPoliglota.buscarRecomendacion(listaRecomendaciones, usuario)

            // Assert
            resultado shouldContainExactly listOf(recomendacion3)
        }
    }
})

class NativistaTesting : DescribeSpec({

    describe("Testeo perfil Nativista") {

        it("Devuelve recomendaciones que tengan un libro con idioma nativo del usuario igual al idiomanativo del autor") {
            // Arrange
            val usuario = crearUsuario(idiomaNativo = Lenguaje.ESPANOL)
            val autor1 = Autor("Autor1", "Apellido1", Lenguaje.ESPANOL, edad = 40,  true, "")
            val autor2 = Autor("Autor2", "Apellido2", Lenguaje.INGLES, edad = 35, true, "")
            val libro1 = crearLibro("Libro1", autor = autor1)
            val libro2 = crearLibro("Libro2", autor = autor2)
            val libro3 = crearLibro("Libro1", autor = autor2)
            val recomendacion1 = crearRecomendacion("Mi recomendación 2", publico = true, usuario, mutableListOf(libro1, libro2), "Esta es una excelente recomendación")
            val recomendacion2 = crearRecomendacion("Mi recomendación 2", publico = true, usuario, mutableListOf(libro2, libro3), "Esta es una excelente recomendación")
            val listaDeRecomendacion = listOf(recomendacion1, recomendacion2)
            val nativista = Nativista()

            // Act
            val resultado = nativista.buscarRecomendacion(listaDeRecomendacion, usuario)

            // Assert
            assertEquals(1, resultado.size)
            assertEquals(recomendacion1, resultado[0])
        }
    }

})

class ExperimentadoTesting : DescribeSpec({

    describe("Testeo perfil Experimentado") {
        it("Devuelve recomendaciones con la mayoría de libros de autores consagrados") {
            // Arrange
            val autorConsagrado = Autor("Consagrado", "Apellido", Lenguaje.ESPANOL, 60, true, "")
            val autorNoConsagrado = Autor("NoConsagrado", "Apellido", Lenguaje.INGLES, 30, false, "")
            val usuario = crearUsuario()
            val libroConsagrado1 = crearLibro(autor = autorConsagrado)
            val libroConsagrado2 = crearLibro(autor = autorConsagrado)
            val libroNoConsagrado1 = crearLibro(autor = autorNoConsagrado)
            val libroNoConsagrado2 = crearLibro(autor = autorNoConsagrado)
            val recomendacion1 = crearRecomendacion("Recomendación 1",publico = true,usuario,mutableListOf(libroConsagrado1, libroConsagrado2, libroNoConsagrado1),"Esta es una excelente recomendación")
            val recomendacion2 = crearRecomendacion("Recomendación 2", publico = true, usuario, mutableListOf(libroNoConsagrado1, libroNoConsagrado2),"Otra excelente recomendación")
            val recomendaciones = listOf(recomendacion1, recomendacion2)
            val perfilExperimentado = Experimentado()

            // Act
            val resultado = perfilExperimentado.buscarRecomendacion(recomendaciones, usuario)

            // Assert
            resultado shouldContainExactly listOf(recomendacion1)

        }
    }
})

class CambianteTesting : DescribeSpec({

    describe("Testeo perfil Cambiante") {

        it("Si el usuario tiene menos de 25 años es leedor") {
            // Arrange
            val autor = crearAutor()
            val usuario = crearUsuario(fechaNacimiento = LocalDate.now().minusYears(25))
            val libro1 = crearLibro(autor = autor)
            val libro2 = crearLibro(autor = autor)
            val libro3 = crearLibro(autor = autor)
            val libro4 = crearLibro(autor = autor)
            val recomendacion1 = crearRecomendacion("Recomendacion 1", true, usuario, mutableListOf(libro1, libro2), "Descripción")
            val recomendacion2 = crearRecomendacion("Recomendacion 2", true, usuario, mutableListOf(libro1, libro3), "Descripción")
            val recomendacion3 = crearRecomendacion("Recomendacion 3", true, usuario, mutableListOf(libro3, libro4), "Descripción")

            val listaDeRecomendacion = listOf(recomendacion1, recomendacion2, recomendacion3)
            val cambiante = Cambiante()

            // Act
            val resultado = cambiante.buscarRecomendacion(listaDeRecomendacion, usuario)

            // Assert
            assertEquals(listaDeRecomendacion, resultado)
        }

        it("Si el usuario tiene mas de 25 años es calculador") {
            // Arrange
            val autor = crearAutor()
            val usuario = crearUsuario(palabrasPorMinuto = 100, fechaNacimiento = LocalDate.now().minusYears(60))
            val libro1 = crearLibro(autor = autor, cantidadPalabras = 1000000) // Tarda 10.000
            val libro2 = crearLibro(autor = autor, cantidadPalabras = 100000)  // Tarda 1000
            val libro3 = crearLibro(autor = autor, cantidadPalabras = 5000000) // Tarda 50.000
            val libro4 = crearLibro(autor = autor, cantidadPalabras = 5000000) // Tarda 50.000
            val recomendacion1 = crearRecomendacion("Recomendacion 1", true, usuario, mutableListOf(libro1, libro2), "Descripción")
            val recomendacion2 = crearRecomendacion("Recomendacion 2", true, usuario, mutableListOf(libro1, libro3), "Descripción")
            val recomendacion3 = crearRecomendacion("Recomendacion 3", true, usuario, mutableListOf(libro3, libro4), "Descripción")

            val listaDeRecomendacion = listOf(recomendacion1, recomendacion2, recomendacion3)
            val cambiante = Cambiante()

            // Act
            val resultado = cambiante.buscarRecomendacion(listaDeRecomendacion, usuario)

            // Assert
            resultado shouldContainAll listOf(recomendacion1)
        }

    }
})

class CalculadorTesting : DescribeSpec({

    describe("Testeo del perfil Calculador") {
        it("Devuelve recomendaciones con tiempo de lectura en el rango adecuado") {
            // Arrange
            val usuario = crearUsuario(
                palabrasPorMinuto = 50, tipoLector = LectorPromedio(),rangoMinimoLectura = 20.0, rangoMaximoLectura = 100.0)
            val autor = Autor("nombre", "apellido", Lenguaje.ESPANOL, 50, true, "")
            val libro1 = crearLibro("Libro1",500,599,autor = autor)
            val libro2 = crearLibro("Libro2",500,599,autor = autor)
            val libro3 = crearLibro("Libro3",10000,599,autor = autor)
            val recomendacion1 = crearRecomendacion(
                "Mi recomendación 1",
                publico = true,
                usuario,
                mutableListOf(libro1, libro2),
                "Esta es una excelente recomendación"
            )
            val recomendacion2 = crearRecomendacion(
                "Mi recomendación 2",
                publico = true,
                usuario,
                mutableListOf(libro2, libro3),
                "Esta es una excelente recomendación"
            )
            val recomendacion3 = crearRecomendacion(
                "Mi recomendación 3",
                publico = true,
                usuario,
                mutableListOf(libro1, libro2, libro3),
                "Esta es una excelente recomendación"
            )
            val listaDeRecomendacion = mutableListOf(recomendacion1, recomendacion2, recomendacion3)
            val calculador = Calculador()

            // Act
            val resultado = calculador.buscarRecomendacion(listaDeRecomendacion, usuario)

            // Assert
            resultado shouldContainAll listOf(recomendacion1)
        }
    }
})

class DemandanteTesting: DescribeSpec({
    it ("Recomendaciones que tengan una valoración de entre 4 y 5 puntos"){
        // Arrange
        val usuario1 = crearUsuario()
        val usuario2 = crearUsuario()
        val autor1 = crearAutor()
        val libro1 = crearLibro(autor = autor1)
        val libro2 = crearLibro(autor = autor1)
        val libro3 = crearLibro(autor = autor1)
        val recomendacion1 = crearRecomendacion("Recomendacion 1", true, usuario1, mutableListOf(libro1, libro2), "Descripción")
        val recomendacion2 = crearRecomendacion("Recomendacion 1", true, usuario1, mutableListOf(libro2, libro3), "Descripción")

        // Act
        usuario2.leerLibro(libro1)
        usuario2.leerLibro(libro2)
        usuario2.leerLibro(libro3)

        recomendacion1.agregarValoracion(usuario2,2.0,"Muy Mala")
        recomendacion2.agregarValoracion(usuario2,4.8,"Muy Buena")

        val listaDeRecomendacion = mutableListOf(recomendacion1, recomendacion2)
        val demandante = Demandante()

        // Act
        val resultado = demandante.buscarRecomendacion(listaDeRecomendacion, usuario2)

        // Assert
        resultado shouldContainAll listOf(recomendacion2)
    }
})

class PerfilDeAceptacionTesting : DescribeSpec({
    describe("Buscar recomendaciones según perfiles a tolerar") {
        it("Debería filtrar las recomendaciones según los perfiles a tolerar") {
            // Arrange
            val perfilesATolerar = listOf(Experimentado(), Demandante())
            val usuario1 = crearUsuario(tipoPerfil = PerfilDeAceptacion(perfilesATolerar))

            val usuario2 = crearUsuario()

            val autorConsagrado = Autor("Consagrado", "Apellido", Lenguaje.ESPANOL, 60, true,"")
            val autorNoConsagrado = Autor("NoConsagrado", "Apellido", Lenguaje.INGLES, 30, false,"")

            val libro1 = crearLibro(autor = autorConsagrado)
            val libro2 = crearLibro(autor = autorNoConsagrado)
            val libro3 = crearLibro(autor = autorNoConsagrado)
            val libro4 = crearLibro(autor = autorNoConsagrado)
            val recomendacion1 = crearRecomendacion("Recomendacion 1", true, usuario1, mutableListOf(libro2, libro4), "Descripción")
            val recomendacion2 = crearRecomendacion("Recomendacion 2", true, usuario1, mutableListOf(libro2, libro3), "Descripción")
            val recomendacion3 = crearRecomendacion("Recomendacion 3", true, usuario1, mutableListOf(libro1, libro3), "Descripción")
            // Act
            usuario2.leerLibro(libro1)
            usuario2.leerLibro(libro2)
            usuario2.leerLibro(libro3)
            usuario2.leerLibro(libro4)

            recomendacion1.agregarValoracion(usuario2,2.0,"Muy Mala")
            recomendacion2.agregarValoracion(usuario2,4.8,"Muy Buena")
            recomendacion3.agregarValoracion(usuario2,2.0,"Muy Mala")



            val recomendaciones = listOf(recomendacion1, recomendacion2, recomendacion3)

            // Act

            val resultado = usuario1.tipoPerfil.buscarRecomendacion(recomendaciones, usuario1)

            // Assert
            resultado.shouldContainExactlyInAnyOrder(recomendacion2, recomendacion3)



        }
    }
})