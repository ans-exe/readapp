package AppDeLibros
import java.util.*

abstract class CentroLectura(
    val direccion: String,
    val libro: String,
    val duracionEncuentroHoras: Int,
    val fechasEncuentro: List<Date>,
    val cantidadParticipantes: Int,
    val costoDivulgacion: Double = 1000.00,
) :RepositorioDatos{
    abstract fun calcularCostoReserva(): Double
    abstract fun calcularMaximoParticipantes(): Int

    open fun expiro(): Boolean {
        return (vencioFecha() || alcanzoMaximoParticipantes())
    }

    fun alcanzoMaximoParticipantes():Boolean{
        return cantidadParticipantes >= calcularMaximoParticipantes()
    }

    fun vencioFecha(): Boolean {
        val fechaActual = Date()
        return fechasEncuentro.all { it.before(fechaActual) }

    }
    abstract fun reservaDisponible(): Boolean
}


class CentroParticular(
    direccion: String,
    libro: String,
    duracionEncuentroHoras: Int,
    fechasEncuentro: List<Date>,
    cantidadParticipantes: Int,
    val cantidadMaximaParticipantes: Int,
    private val porcentajeSinAdicion: Double,
    private val costoAdicionalParticipante: Double = 500.0,
    override var id: Int,
) : CentroLectura(direccion, libro, duracionEncuentroHoras, fechasEncuentro, cantidadParticipantes) {

    override fun calcularCostoReserva(): Double {
        return (cantidadParticipantes * costoDivulgacion + arrastreDeCosto())
    }

    private fun arrastreDeCosto(): Double{
        return ((participantesConRecargo()) * costoAdicionalParticipante)
    }
    private fun participantesSinRecargo(): Double{
        return ((porcentajeSinAdicion/100) * cantidadMaximaParticipantes)
    }
    private fun participantesConRecargo(): Double{
        return (cantidadMaximaParticipantes - participantesSinRecargo())
    }

    override fun calcularMaximoParticipantes(): Int {
        return cantidadMaximaParticipantes
    }

    override fun reservaDisponible(): Boolean {
        return (cantidadParticipantes < cantidadMaximaParticipantes)
    }

    override fun searchCondition(texto: String): Boolean {
        TODO("Not yet implemented")
    }
}

class CentroEditorial(
    direccion: String,
    libro: String,
    duracionEncuentroHoras: Int,
    fechasEncuentro: List<Date>,
    cantidadParticipantes: Int,
    private val montoMinimoCubrir: Double,
    private val costoAdicional: Double = 800.0,
    private val esBestSeller: Boolean = false,
    private val ventasSemanales: Int = 0,
    override var id: Int,


    ) : CentroLectura(direccion, libro, duracionEncuentroHoras, fechasEncuentro, cantidadParticipantes) {

    override fun calcularCostoReserva(): Double {
        var costoTotal = costoDivulgacion + costoAdicional
        costoTotal += if (esBestSeller) {
            ventasSemanales * 0.1 // 10% de las ventas semanales
        } else {
            200.0 // Adición de $200 si no es best seller
        }
        return costoTotal
    }

    override fun calcularMaximoParticipantes(): Int {
        return (montoMinimoCubrir / calcularCostoReserva()).toInt()
    }

    override fun reservaDisponible(): Boolean {
        return cantidadParticipantes <= calcularMaximoParticipantes()
    }

    override fun searchCondition(texto: String): Boolean {
        TODO("Not yet implemented")
    }

}

class CentroBiblioteca(
    direccion: String,
    libro: String,
    duracionEncuentroHoras: Int,
    fechasEncuentro: List<Date>,
    cantidadParticipantes: Int,
    private val metrosCuadradosSala: Double,
    private val gastosFijos: List<Double>,
    private val margenVariablePorDia: Double = 0.1,
    private val margenFijoDespues5Encuentros: Double = 0.5,
    override var id: Int,
) : CentroLectura(direccion, libro, duracionEncuentroHoras, fechasEncuentro, cantidadParticipantes) {

    override fun calcularCostoReserva(): Double {
        val costoTotal = costoDivulgacion + (totalGastosFijos() + calculoMargen())

        return costoTotal / cantidadParticipantes
    }
    fun margenPocosDias() : Double{
        return totalGastosFijos() * margenVariablePorDia * cantidadFechasEncuentro()
    }

    fun margenMuchosDias() : Double{
        return totalGastosFijos() * margenFijoDespues5Encuentros
    }
    private fun calculoMargen() : Double {
        return if (fechasEncuentro.size < 5) {
            margenPocosDias()
        } else {
            margenMuchosDias()
        }
    }

    fun totalGastosFijos() : Double {
        return gastosFijos.sum()
    }
    fun cantidadFechasEncuentro() : Int{
        return fechasEncuentro.size
    }

    override fun calcularMaximoParticipantes(): Int {
        return metrosCuadradosSala.toInt()
    }

    override fun reservaDisponible(): Boolean {
        return cantidadParticipantes < calcularMaximoParticipantes()
    }

    override fun searchCondition(texto: String): Boolean {
        TODO("Not yet implemented")
    }
}