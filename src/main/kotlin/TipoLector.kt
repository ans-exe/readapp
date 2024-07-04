// RECORDATORIO:
// EL TIEMPO DE LECTURA PROMEDIO ES:
// CANTIDAD DE PALABRAS DEL LIBRO / CANTIDAD DE PALABRAS QUE LEE POR MINUTO
// ALGUIEN QUE LEE 50 PALABRAS POR MINUTO, LEE UN LIBRO DE 500 PALABRAS EN 10 MINUTOS

package AppDeLibros

abstract class TipoLector {
    abstract fun calcularTiempo(usuario: Usuario, libro: Libro): Double
}

class LectorPromedio(): TipoLector(){
    override fun calcularTiempo(usuario: Usuario, libro: Libro): Double =
        usuario.tiempoLecturaPromedio(libro).toDouble()
}

class Ansioso(): TipoLector(){
    override fun calcularTiempo(usuario: Usuario, libro: Libro): Double {
        return if (libro.esBestSeller()) {
            (usuario.tiempoLecturaPromedio(libro) * 0.5)
        } else {
            (usuario.tiempoLecturaPromedio(libro) - usuario.tiempoLecturaPromedio(libro) * 0.2)
        }
    }
}

class Fanatico(): TipoLector(){
    override fun calcularTiempo(usuario: Usuario, libro: Libro): Double {
       return if (checkearAutorYSiLeyo(usuario, libro)){
           seTomaSuTiempo(usuario, libro)
        } else {
           usuario.tiempoLecturaPromedio(libro).toDouble()
       }
    }

    // CHECKEA SI EL AUTOR ES UNO DE SUS PREFERIDOS Y SI NO LEYO EL LIBRO
    private fun checkearAutorYSiLeyo(usuario: Usuario, libro: Libro): Boolean{
       return (usuario.autoresPreferidos.contains(libro.autor) && !usuario.leyoElLibro(libro))
    }

    // TIEMPO DE LECTURA AUMENTA A RAZON DE 2 MIN POR PAGINA SI NO ES LARGO
    // SI ES LARGO, SON 2 MIN HASTA DONDE SE CONSIDERA LARGO, DESPUES ES 1 MIN.
    private fun seTomaSuTiempo(usuario: Usuario, libro: Libro): Double {
        return if(!libro.esLargo()){
           (usuario.tiempoLecturaPromedio(libro) + 2 * libro.cantidadPaginas).toDouble()
        } else {
            ((usuario.tiempoLecturaPromedio(libro) + (2 * libro.paginasParaSerLargo) + (libro.cantidadPaginas - libro.paginasParaSerLargo)).toDouble())
        }
    }
}


class Recurrente(): TipoLector(){
    override fun calcularTiempo(usuario: Usuario, libro: Libro): Double{
        var tiempoPorLectura = usuario.tiempoLecturaPromedio(libro).toDouble()

        val vecesLeido = usuario.vecesLeido(libro)

        tiempoPorLectura = ajustarVelocidadPorRecurrencia(tiempoPorLectura, vecesLeido)

        return tiempoPorLectura
    }

    private fun ajustarVelocidadPorRecurrencia(tiempoPorLectura: Double, vecesLeido: Int): Double {
        var tiempoAjustado = tiempoPorLectura

        // Si el libro se ha leído más de 5 veces, no se realiza más ajuste
        if (vecesLeido > 5) return tiempoAjustado

        // Disminuir la velocidad de lectura en un 1% por cada lectura adicional del mismo libro
        for (i in 1..vecesLeido) {
            tiempoAjustado *= 0.99 // Disminuir en un 1% la velocidad de lectura
        }

        return tiempoAjustado
    }
}


