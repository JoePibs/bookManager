package bookManager.domain.port

import bookManager.domain.model.Book

interface BookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
}
