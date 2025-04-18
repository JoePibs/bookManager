package bookManager.infrastructure.driving.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import bookManager.application.CreateBookUseCase
import bookManager.application.ListBooksUseCase
import bookManager.infrastructure.driving.controller.dto.BookDTO
import bookManager.infrastructure.driving.controller.dto.toDTO
import bookManager.infrastructure.driving.controller.dto.toCommand

@RestController
@RequestMapping("/books")
class BookController(
    private val listBooksUseCase: ListBooksUseCase,
    private val createBookUseCase: CreateBookUseCase
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
}
