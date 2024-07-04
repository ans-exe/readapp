import AppDeLibros.*

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class RepositorioTests : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Test Repositorios"){

        val autor1 = crearAutor(apellido = "Perez")
        val autor2 = crearAutor(apellido = "Lopez")
        val autor3 = crearAutor(apellido = "Rodriguez")
        val libro1 = crearLibro(titulo = "Titulo1", autor = autor1)
        val libro2 = crearLibro(titulo = "Titulo2", autor = autor2)
        val libro3 = crearLibro(titulo = "Titulo3", autor = autor3)
        val usuario1 = crearUsuario("Juan","Lopez","JuanLo")
        val usuario2 = crearUsuario("Hernan","Diaz","H_D")
        val usuario3 = crearUsuario("Junior","Lopez","JuLo")

        describe("Repositorio de Libros") {

            // Arrange
            val repositorioDeLibros = Repositorio<Libro>()
            repositorioDeLibros.create(libro1)
            repositorioDeLibros.create(libro2)
            repositorioDeLibros.create(libro3)

            it("El id del ultimo libro debe ser el ultimo id que se genero.") {
                // Assert
                libro3.id shouldBe 3
            }

            it("Al borrar un libro, no debe existir en el repositorio.") {
                // Act
                repositorioDeLibros.delete(libro1)
                // Assert
                shouldThrow<NotFoundException> {
                    repositorioDeLibros.getById(libro1.id)
                }.message shouldBe ErrorMessages.ID_INEXISTENTE
            }

            it("Busqueda parcial de nombre.") {
                // Act
                repositorioDeLibros.search("1").shouldContain(libro1)
            }

            it("Busqueda exacta de autor.") {
                // Act
                repositorioDeLibros.search("Rodriguez").shouldContain(libro3)
            }

            it("Busqueda de cualquier otra cosa, no encuentra nada.") {
                // Act
                repositorioDeLibros.search("@").shouldBeEmpty()
            }
        }

        describe("Repositorio de Usuarios") {
            val repositorioDeUsuarios = Repositorio<Usuario>()
            repositorioDeUsuarios.create(usuario1)
            repositorioDeUsuarios.create(usuario2)
            repositorioDeUsuarios.create(usuario3)

            it("Busqueda parcial de nombre."){
                // Act
                repositorioDeUsuarios.search("Ju") shouldHaveSize 2
            }

            it("Busqueda exacta de username."){
                // Act
                repositorioDeUsuarios.search("H_D") shouldContain(usuario2)
            }
        }

    }

})
