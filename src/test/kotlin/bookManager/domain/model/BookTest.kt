package bookManager.domain.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.assertions.throwables.shouldThrow

class BookTest : FunSpec({

    test("titre vide → IllegalArgumentException") {
        shouldThrow<IllegalArgumentException> { Book("", "Author")
            }
    }

    test("auteur vide → IllegalArgumentException") {
        shouldThrow<IllegalArgumentException> { Book("Title", "")
            }
    }
})
