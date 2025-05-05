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
    fun `POST books should create book and return 201`() {
        val dto = BookDTO("Pney de Plouf", "Author")
        val expectedBook = Book(dto.title, dto.author)
        val expectedCommand = CreateBookCommand(dto.title, dto.author)

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
                it.title == dto.title && it.author == dto.author
            })
        }
    }

    @Test
    fun `GET books should return list of books`() {
        val books = listOf(
            Book("1984", "George Orwell"),
            Book("Le Petit Prince", "Antoine de Saint-Exupéry")
        )
        val expectedDTOs = books.map { BookDTO(it.title, it.author) }

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
    }
}
