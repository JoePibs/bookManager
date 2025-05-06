package bookManager.infrastructure.driving.controller

import bookManager.application.CreateBookUseCase
import bookManager.application.ListBooksUseCase
import bookManager.domain.CreateBookCommand
import bookManager.domain.model.Book
import bookManager.domain.usecase.BookService
import bookManager.infrastructure.driving.controller.dto.BookDTO
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import com.ninjasquad.springmockk.MockkBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get

@ActiveProfiles("test")
@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var createBookUseCase: CreateBookUseCase

    @MockkBean
    lateinit var listBooksUseCase: ListBooksUseCase

    @MockkBean
    lateinit var bookService: BookService

    @Test
    fun `POST Doit creer un livre and return une 201`() {
        val dto = BookDTO("Pney de Plouf", "Author", false)
        val expectedBook = Book(dto.title, dto.author,dto.isReserved)
        val expectedCommand = CreateBookCommand(dto.title, dto.author, dto.isReserved)

        every { createBookUseCase.invoke(expectedCommand) } returns expectedBook

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }.andExpect {
            status { isCreated() }
            content {
                contentType(MediaType.APPLICATION_JSON)
                json(objectMapper.writeValueAsString(dto))
            }
        }

        verify(exactly = 1) {
            createBookUseCase.invoke(match {
                it.title == dto.title && it.author == dto.author && it.is_reserved == dto.isReserved
            })
        }
        println("✅ Livre crée avec une 201 ! 📘✨")

    }

    @Test
    fun `GET Retourne un liste de livres`() {
        val books = listOf(
            Book("1984", "George Orwell",false),
            Book("Le Petit Prince", "Antoine de Saint-Exupéry",false)
        )
        val expectedDTOs = books.map { BookDTO(it.title, it.author, it.isReserved) }

        every { listBooksUseCase.invoke() } returns books

        mockMvc.get("/books")
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(expectedDTOs))
                }
            }

        verify(exactly = 1) { listBooksUseCase.invoke() }
        println("✅ Liste de livre ajouté! 📘✨")

    }

    @Test
    fun `POST réserver le livre et retourner 200 OK`() {
        val title = "Dune"

        every { bookService.reserveBook(title) } returns Unit

        mockMvc.post("/books/$title/reserve")
            .andExpect {
                status { isOk() }
                content {
                    contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    string("Le livre a été réservé avec succès")
                }
            }

        verify(exactly = 1) { bookService.reserveBook(title) }
        println("✅ Livre réservé avec un 200 OK ! 📘✨")

    }

    @Test
    fun `POST retourner 404 si le livre n’existe pas`() {
        val title = "Inexistant"
        every { bookService.reserveBook(title) } throws IllegalArgumentException("Livre non trouvé")

        mockMvc.post("/books/$title/reserve")
            .andExpect {
                status { isNotFound() }
                content {
                    contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    string("Livre non trouvé")
                }
            }

        verify(exactly = 1) { bookService.reserveBook(title) }
        println("✅ Errare Humanum es ...resa echec 402 livre not found ! 📘✨")

    }

    @Test
    fun `POST retourner 409 si le livre est déja reserver`() {
        val title = "Dune"
        every { bookService.reserveBook(title) } throws IllegalStateException("Le livre est déjà réservé")

        mockMvc.post("/books/$title/reserve")
            .andExpect {
                status { isConflict() }
                content {
                    contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    string("Le livre est déjà réservé")
                }
            }

        verify(exactly = 1) { bookService.reserveBook(title) }
        println("✅ quelqu'un a deja pris le livre, petite licorne ! 📘✨")

    }

}
