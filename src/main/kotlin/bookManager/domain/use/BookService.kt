package bookManager.domain.usecase

import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository

class BookService(private val repository: BookRepository) {

    fun addBook(book: Book) {
        repository.save(book)
    }

    fun getAllBooksSorted(): List<Book> {
        return repository.findAll().sortedBy { it.title }
    }
}
