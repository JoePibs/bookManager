package bookManager.domain.usecase

import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val repository: BookRepository) {

    fun addBook(book: Book) {
        repository.save(book)
    }

    fun getAllBooksSorted(): List<Book> {
        return repository.findAll().sortedBy { it.title }
    }

    fun reserveBook(title: String) {
        val book = repository.findByTitle(title)
            ?: throw IllegalArgumentException("Livre avec le titre '$title' introuvable")

        if (book.isReserved) {
            throw IllegalStateException("Le livre est déjà réservé")
        }

        val reservedBook = book.copy(isReserved = true)
        repository.save(reservedBook)
    }

}
