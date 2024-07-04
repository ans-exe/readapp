import AppDeLibros.*

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SendMailTesting : DescribeSpec({

    isolationMode = IsolationMode.InstancePerTest

    describe("Requerimientos de Notificacion por Mail") {
        // ARRANGE
        val autor = crearAutor()

        val libro1 = crearLibro("Titulo1",autor = autor)
        val libro2 = crearLibro("Titulo2",autor = autor)
        val libro3 = crearLibro("Titulo3",autor = autor)

        val listaDeLibrosLeidos = mutableMapOf(libro1 to 90,libro2 to 1, libro3 to 1)
        val listaDeLibros = mutableListOf(libro1,libro2)

        val usuarioAmigo = crearUsuario(
            username = "User2", correo = "user2@gmail.com", librosLeidos = listaDeLibrosLeidos)

        val usuarioCreador = crearUsuario(
            username = "User1", correo = "user1@gmail.com", librosLeidos = listaDeLibrosLeidos,
            listaDeAmigos = mutableListOf(usuarioAmigo)
        )

        val recomendacion1 = crearRecomendacion(
            creador = usuarioCreador, librosRecomendados = (listaDeLibros))

        it("Usuario amigo agrega un libro, se notifica al creador") {
            val notificarCreador = NotificarCreador(StubMailSender, notificationSenderMail)

            usuarioCreador.addAction(notificarCreador)
            StubMailSender.reset()

            // ACT
            recomendacion1.agregarLibro(usuarioAmigo,libro3)

            // ASSERT
            StubMailSender.mailsSent.size.shouldBe(1)
        }

        it("Usuario creador agrega un libro, NO se notifica al creador") {
            val notificarCreador = NotificarCreador(StubMailSender, notificationSenderMail)
            usuarioCreador.addAction(notificarCreador)
            StubMailSender.reset()

            // ACT
            recomendacion1.agregarLibro(usuarioCreador,libro3)

            // ASSERT
            StubMailSender.mailsSent.size.shouldBe(0)
        }

        it("Usuario amigo agrega un libro, pero el creador no activo, NO se notifica al creador") {
            val notificarCreador = NotificarCreador(StubMailSender, notificationSenderMail)
            usuarioCreador.addAction(notificarCreador)
            usuarioCreador.removeAction(notificarCreador)
            StubMailSender.reset()

            // ACT
            recomendacion1.agregarLibro(usuarioAmigo,libro3)

            // ASSERT
            StubMailSender.mailsSent.size.shouldBe(0)
        }

        it("Corroborar que el contenido del mail enviado sea el correcto") {
            val notificarCreador = NotificarCreador(StubMailSender, notificationSenderMail)
            usuarioCreador.addAction(notificarCreador)
            StubMailSender.reset()

            // ACT
            recomendacion1.agregarLibro(usuarioAmigo,libro3)

            // ASSERT
            val sentMail = StubMailSender.mailsSent.first()
            sentMail.from shouldBe notificationSenderMail
            sentMail.to shouldBe "user1@gmail.com"
            sentMail.subject shouldBe "Se agregó un Libro"
            sentMail.content shouldBe """
            |El usuario: User2 agregó el libro Titulo3 a la recomendación 
            |que tenía estos títulos: Titulo1, Titulo2.
        """.trimMargin()
        }
    }
})