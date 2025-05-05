package bookManager.application

import bookManager.domain.port.BookRepository
import bookManager.domain.model.Book
import bookManager.domain.CreateBookCommand

open class CreateBookUseCase(private val bookRepository: BookRepository) {
    operator fun invoke(cmd: CreateBookCommand): Book {
        return bookRepository.save(
            Book(
                title = cmd.title,
                author = cmd.author
            )
        )
    }
}
