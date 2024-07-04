package AppDeLibros

class Admin {
    var processStarted: Boolean = false
    val procesos = mutableListOf<Proceso>()
    fun addProcess(proc: Proceso) {
        procesos.add(proc)
    }

    fun removeProcess(proc: Proceso) {
        procesos.remove(proc)
    }

    fun run() {
        if (procesos.isEmpty()) throw BusinessException(ErrorMessages.PROC_NOT_FOUND)
        procesos.forEach { it.execute() }
    }
}

