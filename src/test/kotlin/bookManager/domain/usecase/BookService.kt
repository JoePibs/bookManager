package bookManager.domain.usecase

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository
import bookManager.domain.usecase.BookService

class BookServiceTest : FunSpec({

    val repository = mockk<BookRepository>(relaxUnitFun = true)
    val service = BookService(repository)

    test("ajoute un livre via le dépôt") {
        println("▶️ Test : ajout d'un livre via le dépôt")
        val book = Book("1984", "George Orwell")
        service.addBook(book)
        verify { repository.save(book) }
        println("✅ Livre ajouté avec succès")
    }

    test("retourne tous les livres triés par titre") {
        println("▶️ Test : tri des livres par titre")
        val books = listOf(
            Book("Zebra Book", "Author Z"),
            Book("Apple Book", "Author A"),
            Book("Mango Book", "Author M")
        )
        every { repository.findAll() } returns books

        val result = service.getAllBooksSorted()

        result shouldContainExactly listOf(
            Book("Apple Book", "Author A"),
            Book("Mango Book", "Author M"),
            Book("Zebra Book", "Author Z")
        )
        println("✅ Tri effectué correctement")
    }

    test("lance une exception si le titre ou l'auteur est vide") {
        println("▶️ Test : validation des champs obligatoires")
        shouldThrow<IllegalArgumentException> { Book("", "Author") }
        shouldThrow<IllegalArgumentException> { Book("Title", "") }
        println("✅ Les champs vides sont bien refusés")
    }
})
