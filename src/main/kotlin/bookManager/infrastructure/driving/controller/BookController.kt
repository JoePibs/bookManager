package bookManager.infrastructure.driving.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import bookManager.application.CreateBookUseCase
import bookManager.application.ListBooksUseCase
import bookManager.infrastructure.driving.controller.dto.BookDTO
import bookManager.infrastructure.driving.controller.dto.toDTO
import bookManager.infrastructure.driving.controller.dto.toCommand
import bookManager.domain.usecase.BookService


@RestController
@RequestMapping("/books")
class BookController(
    private val listBooksUseCase: ListBooksUseCase,
    private val createBookUseCase: CreateBookUseCase,
    private val bookService: BookService
) {

    @GetMapping
    fun getAll(): List<BookDTO> =
        listBooksUseCase().map { it.toDTO() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody dto: BookDTO): BookDTO {
        val created = createBookUseCase(dto.toCommand())
        return created.toDTO()
    }

    @PostMapping("/{title}/reserve")
    fun reserveBook(@PathVariable title: String): ResponseEntity<String> {
        return try {
            bookService.reserveBook(title)
            ResponseEntity.ok("Le livre a été réservé avec succès")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre non trouvé")
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("Le livre est déjà réservé")
        }
    }

}
