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
        val sql = "INSERT INTO books (title, author,is_reserved) VALUES (:title, :author, :isReserved)"
        val params = mapOf(
            "title" to book.title,
            "author" to book.author,
            "isReserved" to book.isReserved
        )

        jdbcTemplate.update(sql, params)
        return book
    }

    override fun findAll(): List<Book> {
        val sql = "SELECT * FROM books ORDER BY title"
        return jdbcTemplate.query(sql, emptyMap<String, Any>()) { rs, _ ->
            Book(rs.getString("title"), rs.getString("author"), rs.getBoolean("is_reserved"))
        }
    }

    override fun findByTitle(title: String): Book? {
        val sql = "SELECT * FROM books WHERE title = :title"
        val params = mapOf("title" to title)

        val result = jdbcTemplate.query(sql, params) { rs, _ ->
            Book(
                title = rs.getString("title"),
                author = rs.getString("author"),
                isReserved = rs.getBoolean("is_reserved")
            )
        }

        return result.firstOrNull()
    }

}
