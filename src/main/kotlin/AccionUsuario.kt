package AppDeLibros

//------------------------- IMPLEMENTACION DE ACCIONES -------------------------//
interface AccionesUsuario {
    fun ejecutarAccion(usuario: Usuario, libro: Libro, recomendacion: Recomendacion)
}

class NotificarCreador(private val mailSender: MailSender, private val notificationSenderMail: String) : AccionesUsuario {
    override fun ejecutarAccion(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        if (usuario != recomendacion.creador) {
            buildAndSendMail(usuario, libro, recomendacion)
        }
    }

    private fun buildAndSendMail(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        val mail = Mail(
            from = notificationSenderMail,
            to = recomendacion.creador.correo,
            subject = "Se agregó un Libro",
            content = getBody(usuario, libro, recomendacion)
        )
        mailSender.sendMail(mail)
    }

    private fun getBody(usuario: Usuario, libro: Libro, recomendacion: Recomendacion): String {
        val libros = recomendacion.librosRecomendados
            .filter { it != libro }
            .joinToString(", ") {it.titulo}

        return """
            |El usuario: ${usuario.username} agregó el libro ${libro.titulo} a la recomendación 
            |que tenía estos títulos: $libros.
        """.trimMargin()
    }
}

class RegistrarContribuciones(): AccionesUsuario{
    override fun ejecutarAccion(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        recomendacion.contribuciones.computeIfAbsent(usuario){ mutableListOf() }.add(libro)
    }
}

class ControlDeContribuciones():AccionesUsuario{
    override fun ejecutarAccion(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        val contribucionesDelUsuario = recomendacion.contribuciones[usuario]?.size ?: 0

        if(contribucionesDelUsuario >= recomendacion.contribucionesLimite){
            recomendacion.creador.listaDeAmigos.remove(usuario)
        }
    }
}

class CalificacionAutomatica():AccionesUsuario{
    override fun ejecutarAccion(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {

        val valoracionDelUsuario = recomendacion.valoraciones.find { it.usuario == usuario }
        val esCreador = (usuario != recomendacion.creador)
        val creoValoracion = valoracionDelUsuario == null

        if(esCreador && creoValoracion ){
            recomendacion.agregarValoracion(usuario,5.0,"Excelente 100% recomendable")
        }
    }
}