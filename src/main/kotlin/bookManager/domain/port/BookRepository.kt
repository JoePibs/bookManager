package bookManager.domain.port

import bookManager.domain.model.Book

interface BookRepository {
    fun save(book: Book): Book
    fun findByTitle(title: String): Book?
    fun findAll(): List<Book>
}
