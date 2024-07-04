import AppDeLibros.Mail
import AppDeLibros.MailSender


object StubMailSender : MailSender {
    val mailsSent: MutableList<Mail> = mutableListOf()

    override fun sendMail(mail: Mail) {
        mailsSent.add(mail)
    }

    fun reset(){
        mailsSent.clear()
    }
}