package bookManager.infrastructure.driving.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.verify
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import your.package.domain.Book
import your.package.domain.CreateBookCommand
import your.package.infrastructure.application.CreateBookUseCase
import your.package.infrastructure.application.ListBooksUseCase
import your.package.infrastructure.driving.controller.dto.BookDTO

@ExtendWith(MockKExtension::class)
@WebMvcTest(BookController::class)
class BookControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    // remplace @MockBean par @MockkBean si tu utilises mockk-spring
    @MockBean private val listBooksUseCase: ListBooksUseCase,
    @MockBean private val createBookUseCase: CreateBookUseCase
) {
    private val mapper = jacksonObjectMapper()

    @Test
    fun `GET books - cas nominal`() {
        val domainBooks = listOf(
            Book(id = 1, title = "Kotlin in Action", author = "Dmitry"),
            Book(id = 2, title = "Clean Code", author = "Robert")
        )
        every { listBooksUseCase() } returns domainBooks

        mvc.perform(get("/books"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", `is`(2)))
            .andExpect(jsonPath("$[0].id", `is`(1)))
            .andExpect(jsonPath("$[0].title", `is`("Kotlin in Action")))
            .andExpect(jsonPath("$[1].author", `is`("Robert")))

        verify(exactly = 1) { listBooksUseCase() }
    }

    @Test
    fun `POST books - cas nominal`() {
        val dto = BookDTO(title = "Domain-Driven Design", author = "Evans")
        val cmd = CreateBookCommand(title = dto.title, author = dto.author)
        val created = Book(id = 42, title = dto.title, author = dto.author)
        every { createBookUseCase(cmd) } returns created

        mvc.perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id", `is`(42)))
            .andExpect(jsonPath("$.title", `is`("Domain-Driven Design")))

        verify(exactly = 1) { createBookUseCase(cmd) }
    }

    @Test
    fun `POST books - erreur 400 si payload invalide`() {
        // envoi d'un JSON sans champ "title"
        val invalidJson = """{"author":"X"}"""
        mvc.perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `GET books - domaine échoue avec exception`() {
        every { listBooksUseCase() } throws IllegalStateException("DB inaccessible")

        mvc.perform(get("/books"))
            .andExpect(status().is5xxServerError)

        verify { listBooksUseCase() }
    }
}
