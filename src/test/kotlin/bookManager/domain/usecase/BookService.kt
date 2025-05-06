package bookManager.domain.usecase

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository
import bookManager.domain.usecase.BookService
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.arbitrary.filter
import io.mockk.*


class BookServiceTest : FunSpec({

    val repository = mockk<BookRepository>(relaxUnitFun = true)
    val service = BookService(repository)

    val validStrings = Arb.string(1..20).filter { it.isNotBlank() && it.any { c -> !c.isWhitespace() } }

    fun randomBooks(titles: List<String>, authors: List<String>) =
        titles.zip(authors).map { (t, a) -> Book(t, a) }

    beforeTest {
        clearMocks(repository)
    }

    test("ajoute un livre via le dépôt") {
        val book = Book("1984", "George Orwell")
        every { repository.save(book) } returns book

        service.addBook(book)

        verify { repository.save(book) }
    }

    test("retourne tous les livres triés par titre") {
        val books = listOf(
            Book("Zebra Book", "Author Z"),
            Book("Apple Book", "Author A"),
            Book("Mango Book", "Author M")
        )
        every { repository.findAll() } returns books

        val result = service.getAllBooksSorted()

        result shouldBe listOf(
            Book("Apple Book", "Author A"),
            Book("Mango Book", "Author M"),
            Book("Zebra Book", "Author Z")
        )
    }

    test("la liste retournée contient tous les livres stockés (test de propriété)") {
        checkAll(5, Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = randomBooks(titles, authors)
            every { repository.findAll() } returns books

            val result = service.getAllBooksSorted()

            result shouldContainExactlyInAnyOrder books
        }
    }

    test("les livres sont triés par titre (ordre alphabétique)") {
        checkAll(5, Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = randomBooks(titles, authors)
            every { repository.findAll() } returns books

            val result = service.getAllBooksSorted()
            val expected = books.sortedBy { it.title }

            result shouldBe expected
        }
    }

    test("la liste triée contient exactement les mêmes livres que la liste initiale") {
        checkAll(5, Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = randomBooks(titles, authors)
            val sorted = books.sortedBy { it.title }

            sorted shouldContainExactlyInAnyOrder books
        }
    }

    test("réserve un livre non réservé avec succès") {
        val book = Book("Dune", "Frank Herbert", isReserved = false)
        val updatedBook = book.copy(isReserved = true)

        every { repository.findByTitle("Dune") } returns book
        every { repository.save(updatedBook) } returns updatedBook

        service.reserveBook("Dune")

        verify { repository.findByTitle("Dune") }
        verify { repository.save(updatedBook) }
    }

    test("réserver un livre inexistant lève une exception") {
        every { repository.findByTitle("Fantôme") } returns null

        shouldThrow<IllegalArgumentException> {
            service.reserveBook("Fantôme")
        }

        verify { repository.findByTitle("Fantôme") }
    }

    test("réserver un livre déjà réservé lève une exception") {
        val reservedBook = Book("Dune", "Frank Herbert", isReserved = true)

        every { repository.findByTitle("Dune") } returns reservedBook

        shouldThrow<IllegalStateException> {
            service.reserveBook("Dune")
        }

        verify { repository.findByTitle("Dune") }
    }

})
