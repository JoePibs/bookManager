package bookManager.domain.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.assertions.throwables.shouldThrow

class BookTest : FunSpec({

    test("titre vide → IllegalArgumentException") {
        println("🦄Pas de titre vide stp✨")
        shouldThrow<IllegalArgumentException> { Book("", "Author")
            }
    }

    test("auteur vide → IllegalArgumentException") {
        println("🦄Pas d'auteur vide stp✨")
        shouldThrow<IllegalArgumentException> { Book("Title", "")
            }
    }
})
