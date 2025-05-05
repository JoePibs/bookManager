package bookManager.application

import bookManager.domain.port.BookRepository
import bookManager.domain.model.Book

class ListBooksUseCase(private val bookRepository: BookRepository) {
    operator fun invoke(): List<Book> =
        bookRepository.findAll()
}