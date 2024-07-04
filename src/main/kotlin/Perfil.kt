package AppDeLibros

interface IPerfil{
    fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion>
}

class Precavido():IPerfil{

    private fun buscarSiEstaEnPendientes(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { recomendacion ->
            recomendacion.librosRecomendados.any { usuario.librosALeer.contains(it) }
        }
    }

    private fun buscarSiAmigoLeyo(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion>{
        return recomendaciones.filter { recomendacion ->
            recomendacion.librosRecomendados.any { usuario.listaDeAmigos.any{amigo->amigo.leyoElLibro(it)}
            }
        }
    }

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return buscarSiEstaEnPendientes(recomendaciones, usuario) + buscarSiAmigoLeyo(recomendaciones,usuario)

    }
}

class Leedor(): IPerfil{

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion>{
        return recomendaciones
    }
}

class Poliglota : IPerfil {

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { recomendacion ->
            recomendacion.librosRecomendados
                .flatMap { libro -> libro.lenguajesPublicados }
                .distinct()
                .size >= 5
        }
    }
}

class Nativista : IPerfil {

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { recomendacion ->
            recomendacion.librosRecomendados.any { libro ->
                libro.autor.idiomaNativo == usuario.idiomaNativo
            }
        }
    }
}

class Calculador : IPerfil {

    fun calcularTiempoTotalLectura(recomendacion: Recomendacion, usuario: Usuario): Double {
        return recomendacion.librosRecomendados.sumOf { usuario.tipoLector.calcularTiempo(usuario,it) }
    }

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { calcularTiempoTotalLectura(it,usuario) in usuario.rangoMinimoLectura..usuario.rangoMaximoLectura }
    }
}

class Demandante : IPerfil {

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { recomendacion ->
            recomendacion.calcularValoracionPromedio() in 4.0..5.0
        }
    }
}

class Experimentado : IPerfil {

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return recomendaciones.filter { recomendacion ->
            val autoresConsagrados = recomendacion.librosRecomendados.filter { libro ->
                libro.autor.esConsagrado()
            }
            autoresConsagrados.size >= recomendacion.librosRecomendados.size / 2
        }
    }
}

class Cambiante : IPerfil {

    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return if (usuario.edad <= 25) {
            Leedor().buscarRecomendacion(recomendaciones, usuario)
        } else {
            Calculador().buscarRecomendacion(recomendaciones, usuario.copy(rangoMinimoLectura = 10000.00, rangoMaximoLectura = 15000.00))
        }
    }
}

class PerfilDeAceptacion(private val perfilesATolerar: List<IPerfil>) : IPerfil {
    override fun buscarRecomendacion(recomendaciones: List<Recomendacion>, usuario: Usuario): List<Recomendacion> {
        return perfilesATolerar.flatMap { perfil ->
            perfil.buscarRecomendacion(recomendaciones, usuario)
        }.distinct()
    }
}
