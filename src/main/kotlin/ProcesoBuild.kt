package AppDeLibros

class ProcessBuilder(var mailSender: MailSender) {
    val proc = mutableListOf<Proceso>()

    fun borrarUserInactivo(toProcess: UserRepository): ProcessBuilder {
        proc.add(DeleteInactiveUser(toProcess, mailSender))
        return this
    }

    fun actualizacionLibros(toProcess: UpdaterLibros): ProcessBuilder {
        proc.add(ActualizacionLibros(toProcess, mailSender))
        return this
    }

    fun borrarCentrosLecturaExpirados(toProcess: CentroRepository): ProcessBuilder {
        proc.add(BorrarCentrosLecturaExpirados(toProcess, mailSender))
        return this
    }

    fun agregarAutores(toProcess: AutorRepository, autores: MutableList<Autor>): ProcessBuilder {
        proc.add(AgregarAutores(toProcess, autores, mailSender))
        return this
    }

    fun build(): MutableList<Proceso> {
        if (proc.isEmpty()) {
            throw BusinessException("El programa no puede estar vacío")
        }
        return proc
    }
}