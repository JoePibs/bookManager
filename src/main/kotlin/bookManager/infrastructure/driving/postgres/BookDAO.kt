package bookManager.infrastructure.driving.postgres

import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class BookDAO(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) : BookRepository {

    override fun save(book: Book): Book {
        val sql = "INSERT INTO books (title, author) VALUES (:title, :author)"
        val params = mapOf("title" to book.title, "author" to book.author)
        jdbcTemplate.update(sql, params)
        return book
    }

    override fun findAll(): List<Book> {
        val sql = "SELECT * FROM books ORDER BY title"
        return jdbcTemplate.query(sql, emptyMap<String, Any>()) { rs, _ ->
            Book(rs.getString("title"), rs.getString("author"))
        }
    }
}
