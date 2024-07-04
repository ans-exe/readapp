package AppDeLibros

abstract class Proceso() {
    open lateinit var mailSender: MailSender
    val processName = this.javaClass.simpleName

    private fun enviarMail(){
        val mail = Mail(
            to = adminSenderMail,
            from = notificationSenderMail,
            subject = "Se realizó el proceso: $processName",
            content = "Se realizó el proceso: $processName")
        mailSender.sendMail(mail)
    }

    open fun execute(){
        enviarMail()
    }
}

class DeleteInactiveUser(private val toProcess: UserRepository, override var mailSender: MailSender): Proceso() {
//    override fun execute(){
//        toProcess.massiveDelete(toProcess.getInactives())
//        super.execute()
//    }
    override fun execute() {
        val inactives = toProcess.getInactives()
        // Eliminar usuarios inactivos solo si hay alguno
        if (inactives.isNotEmpty()) {
            toProcess.massiveDelete(inactives)
        }
        super.execute()
    }
}

class ActualizacionLibros(private val updaterLibros: UpdaterLibros, override var mailSender: MailSender) : Proceso() {
    override fun execute() {
        updaterLibros.updateLibros()
        super.execute()
    }
}

class BorrarCentrosLecturaExpirados(private val repositorio: CentroRepository, override var mailSender: MailSender): Proceso() {
    override fun execute(){
        repositorio.massiveDelete(repositorio.getInactives())
        super.execute()
    }
}

class AgregarAutores(private val toProcess: AutorRepository,private val autores: MutableList<Autor>,override var mailSender: MailSender): Proceso() {
    override fun execute(){
        toProcess.agregarTodos(autores)
        super.execute()
    }
}


