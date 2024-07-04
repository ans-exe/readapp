import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import AppDeLibros.*

fun crearAutor (
    nombre: String = "",
    apellido: String = "",
    idiomaNativo: Lenguaje = Lenguaje.ESPANOL,
    edad : Int = 0,
    ganoPremios: Boolean = false,
    seudonimo : String = "",
    ): Autor {
    return Autor(
        nombre, apellido, idiomaNativo, edad, ganoPremios, seudonimo,
    )
}