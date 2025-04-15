package bookManager.domain.usecase

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import bookManager.domain.model.Book
import bookManager.domain.port.BookRepository
import bookManager.domain.usecase.BookService
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.arbitrary.filter

class BookServiceTest : FunSpec({

    val repository = mockk<BookRepository>(relaxUnitFun = true)
    val service = BookService(repository)

    val validStrings: Arb<String> = Arb.string(1..20).filter { s: String ->
        s.isNotBlank() && s.any { c -> !c.isWhitespace() }
    }

    test("🦄 ajoute un livre via le dépôt") {
        println("🦄 Ajout d'un livre en cours... ✍️")
        val book = Book("1984", "George Orwell")
        service.addBook(book)
        verify { repository.save(book) }
        println("✅ Livre ajouté avec succès ! 📘✨")
    }

    test("🦄 retourne tous les livres triés par titre") {
        println("🦄 Test de tri magique des livres 📚🔤")
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
        println("✅ Tri alphabétique parfait 🌈📖")
    }

    test("🦄 lance une exception si le titre ou l'auteur est vide") {
        println("🧹 Validation des champs... interdiction de vide !")
        shouldThrow<IllegalArgumentException> { Book("", "Author") }
        shouldThrow<IllegalArgumentException> { Book("Title", "") }
        println("✅ Les livres fantômes sont interdits 👻❌")
    }

    test("🦄 la liste retournée contient tous les livres stockés (test de propriété)") {
        println("🧪 Test de propriété magique : correspondance parfaite 🧙‍♂️")

        checkAll(5,Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = titles.zip(authors).map { (t, a) -> Book(t, a) }
            every { repository.findAll() } returns books
            val result = service.getAllBooksSorted()
            result shouldContainExactlyInAnyOrder books

            println("🦄 ${books.size} livres vérifiés avec succès 🌟📚")
        }
    }

    test("🦄 les livres sont triés par titre (ordre alphabétique)") {
        println("🔠 Vérification de l’ordre magique des titres ✨")

        checkAll(5,Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = titles.zip(authors).map { (t, a) -> Book(t, a) }
            every { repository.findAll() } returns books
            val result = service.getAllBooksSorted()
            val expected = books.sortedBy { it.title }

            result shouldContainExactly expected

            println("📚 ${books.size} livres bien triés par ordre alphabétique 🌈")
        }
    }

    test("🦄 la liste triée contient exactement les mêmes livres que la liste initiale") {
        println("🦄 Test de propriété : intégrité de la liste après tri ✨")

        checkAll(5,Arb.list(validStrings, 1..10), Arb.list(validStrings, 1..10)) { titles, authors ->
            val books = titles.zip(authors).map { (t, a) -> Book(t, a) }
            val sorted = books.sortedBy { it.title }

            sorted shouldContainExactlyInAnyOrder books

            println("🌟 ${books.size} livres triés sans perte de magie 🦄")
        }
    }

})
