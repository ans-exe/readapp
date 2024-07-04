import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.util.*
import AppDeLibros.*

class CentroParticularTest : DescribeSpec({

    val fechaActual = Date()
    val fechasEncuentro = listOf(fechaActual, fechaActual, fechaActual)
    val centroParticular = CentroParticular(
        direccion = "Dirección 1",
        libro = "Libro 1",
        duracionEncuentroHoras = 2,
        fechasEncuentro = fechasEncuentro,
        cantidadMaximaParticipantes = 10,
        porcentajeSinAdicion = 5.0,
        cantidadParticipantes = 10,
        id = 0
    )

    describe("Centro Particular") {

        it("Calcula el costo de reserva") {
            // Act
            val costoReserva = centroParticular.calcularCostoReserva()
            // Assert
            costoReserva shouldBe 14750.0
        }

        it("Verifica correctamente si el centro particular ha expirado") {

            centroParticular.alcanzoMaximoParticipantes() shouldBe true
            centroParticular.expiro() shouldBe true
        }

        it("Verifica correctamente si la reserva está disponible cuando aún hay espacio para más participantes") {
            val centro2 = CentroParticular(
                direccion = "Dirección 1",
                libro = "Libro 1",
                duracionEncuentroHoras = 2,
                fechasEncuentro = fechasEncuentro,
                cantidadMaximaParticipantes = 10,
                porcentajeSinAdicion = 5.0,
                cantidadParticipantes = 8,
                id = 0
            )
            centro2.reservaDisponible() shouldBe true
        }

        it("Verifica correctamente si la reserva no está disponible cuando se alcanza el límite máximo de participantes") {

            centroParticular.reservaDisponible()shouldBe false
        }
    }
})

class CentroEditorialTest : DescribeSpec({

    val fechaActual = Date()
    val fechasEncuentro = listOf(fechaActual, fechaActual, fechaActual)
    val centroEditorial = CentroEditorial(
        direccion = "Dirección 1",
        libro = "Libro 1",
        duracionEncuentroHoras = 2,
        fechasEncuentro = fechasEncuentro,
        montoMinimoCubrir = 20000.0,
        cantidadParticipantes = 10,
        esBestSeller = false,
        ventasSemanales  = 15000,
        id = 0
    )
    val centroEditorial2 = CentroEditorial(
        direccion = "Dirección 1",
        libro = "Libro 1",
        duracionEncuentroHoras = 2,
        fechasEncuentro = fechasEncuentro,
        montoMinimoCubrir = 19800.0,
        cantidadParticipantes = 10,
        esBestSeller = true,
        ventasSemanales  = 15000,
        id=0
    )
    describe("Centro Editorial") {

        it("Calcula correctamente el costo de reserva para un libro NO best seller") {

            centroEditorial.calcularCostoReserva() shouldBe 2000.00
        }

        it("Calcula correctamente el costo de reserva para un libro best seller") {
            centroEditorial2.calcularCostoReserva() shouldBe 3300.0
        }

        it("Calcula correctamente el máximo de participantes, libro NO bets seller") {

            centroEditorial.calcularMaximoParticipantes()shouldBe 10
        }

        it("Calcula correctamente el máximo de participantes, libro bets seller") {

            centroEditorial2.calcularMaximoParticipantes()shouldBe 6
        }

        it("Verifica correctamente si el centro editorial ha expirado") {

            centroEditorial.expiro()shouldBe true
        }

        it("Verifica correctamente si la reserva está disponible cuando aún hay espacio para más participantes") {

            centroEditorial.reservaDisponible() shouldBe true
        }

        it("Verifica correctamente si la reserva no está disponible cuando se alcanza el límite máximo de participantes") {

            centroEditorial2.calcularMaximoParticipantes()
            centroEditorial2.reservaDisponible() shouldBe false
        }
    }
})


class CentroBibliotecaTest : DescribeSpec({

    val fechaActual = Date()
    val fechasEncuentro = listOf(fechaActual, fechaActual, fechaActual, fechaActual)
    val fechasEncuentro1 = listOf(fechaActual, fechaActual, fechaActual, fechaActual,fechaActual, fechaActual, fechaActual, fechaActual,fechaActual, fechaActual)
    val gastosFijos = listOf(10000.0, 5000.0, 15000.0)
    val centroBiblioteca = CentroBiblioteca(
        direccion = "Dirección 1",
        libro = "Libro 1",
        duracionEncuentroHoras = 2,
        fechasEncuentro = fechasEncuentro,
        metrosCuadradosSala = 100.0,
        gastosFijos = gastosFijos,
        cantidadParticipantes = 100,
        id=0
    )

    val centroBiblioteca2 = CentroBiblioteca(
        direccion = "Dirección 1",
        libro = "Libro 1",
        duracionEncuentroHoras = 2,
        fechasEncuentro = fechasEncuentro1,
        metrosCuadradosSala = 150.0,
        gastosFijos = gastosFijos,
        cantidadParticipantes = 100,
        id=0
    )
    describe("Centro Biblioteca") {

        it("Calcula correctamente el costo de reserva para una biblioteca con menos de 5 días de encuentro") {

            centroBiblioteca.totalGastosFijos() shouldBe 30000.0
            centroBiblioteca.cantidadFechasEncuentro() shouldBe 4
            centroBiblioteca.margenPocosDias() shouldBe 12000.0
            centroBiblioteca.calcularCostoReserva() shouldBe 430.0
            // en el ejemplo del campus da 420 pero yo le sume los 1000 de costo de divulgacion
        }

        it("Calcula correctamente el costo de reserva para una biblioteca con 10 dias de encuentro") {

            centroBiblioteca2.totalGastosFijos() shouldBe 30000.0
            centroBiblioteca2.cantidadFechasEncuentro() shouldBe 10
            centroBiblioteca2.margenMuchosDias() shouldBe 15000.0
            centroBiblioteca2.calcularCostoReserva() shouldBe 460.0
        }

        it("Calcula correctamente el máximo de participantes basado en los metros cuadrados de la sala") {

            centroBiblioteca2.calcularMaximoParticipantes() shouldBe 150
        }

        it("Verifica correctamente si el centro biblioteca ha expirado, cantidad maxima de participantes") {

            centroBiblioteca.expiro() shouldBe true
        }

        it("Verifica correctamente si la reserva está disponible cuando aún hay espacio para más participantes") {

            centroBiblioteca2.reservaDisponible() shouldBe true
        }

        it("Verifica correctamente si la reserva no está disponible cuando se alcanza el límite máximo de participantes") {

            centroBiblioteca.reservaDisponible() shouldBe false
        }
    }
})