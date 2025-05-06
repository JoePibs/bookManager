package bookManager.infrastructure.driving.postgres

import bookManager.domain.model.Book
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@JdbcTest
@Import(BookDAO::class)
@ActiveProfiles("test")
class BookDAOTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer("postgres:15").apply {
            withDatabaseName("test")
            withUsername("postgres")
            withPassword("password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    lateinit var dao: BookDAO

    @Test
    fun `save and retrieve books`() {
        val book = Book("Test Title", "Test Author", true)
        dao.save(book)

        val books = dao.findAll()
        Assertions.assertTrue(books.contains(book))
    }
}
