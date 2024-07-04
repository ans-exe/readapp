package AppDeLibros


interface MailSender {
    fun sendMail(mail: Mail)
}

data class Mail(val subject : String, val content : String, val from : String, val to : String)

const val notificationSenderMail : String = "notificaciones@readapp.com.ar"
const val adminSenderMail : String = "admin@readapp.com.ar"