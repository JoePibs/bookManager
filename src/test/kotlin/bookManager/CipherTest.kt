package bookManager

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.checkAll
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.filter
import io.kotest.matchers.char.shouldBeInRange
import io.kotest.matchers.shouldBe

class CipherTest : FunSpec({

    val cipher = Cipher()

    test("chiffrement manuel de 'A' avec décalage 0 à 5") {
        val letter = 'A'
        val expected = listOf('A', 'B', 'C', 'D', 'E', 'F')

        for (shift in 0..5) {
            val result = cipher.cipher(letter, shift)
            println("Décalage $shift : '$letter' -> '$result'")
            result shouldBe expected[shift]
        }

        println("Test OK : chiffrement manuel sur 'A'")
    }

    test("Le caractère non chiffré reste entre A-Z") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(0..100)
        ) { letter, key ->
            cipher.cipher(letter, key).shouldBeInRange('A'..'Z')
        }
        println("Test OK : cipher reste dans A-Z")
    }

    test("le caractère déchiffré reste entre A-Z") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(0..100)
        ) { letter, key ->
            cipher.decipher(letter, key).shouldBeInRange('A'..'Z')
        }
        println("Test OK : decipher reste dans A-Z")
    }

    test("cipher(letter, 0) == letter") {
        checkAll(Arb.char().filter { it in 'A'..'Z' }) { letter ->
            cipher.cipher(letter, 0) shouldBe letter
        }
        println("Test OK : décalage 0")
    }

    test("decipher(letter, 0) == letter") {
        checkAll(Arb.char().filter { it in 'A'..'Z' }) { letter ->
            cipher.decipher(letter, 0) shouldBe letter
        }
        println("Test OK : decipher avec décalage 0")
    }

    test("Cipher + decipher revient au caractère original") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(0..100)
        ) { letter, key ->
            val encrypted = cipher.cipher(letter, key)
            val decrypted = cipher.decipher(encrypted, key)
            decrypted shouldBe letter
        }
        println("✅ Test OK : cipher + decipher = original")
    }

    test("Decipher + cipher revient au caractère original") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(0..100)
        ) { letter, key ->
            val decrypted = cipher.decipher(letter, key)
            val encrypted = cipher.cipher(decrypted, key)
            encrypted shouldBe letter
        }
        println("Test OK : decipher + cipher = original")
    }

    test("décalage multiple de 26 ne change rien") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(1..10)
        ) { letter, k ->
            cipher.cipher(letter, k * 26) shouldBe letter
            cipher.decipher(letter, k * 26) shouldBe letter
        }
        println("Test OK : décalage multiple de 26")
    }

    test("cipher(a) puis cipher(b) == cipher(a + b)") {
        checkAll(
            Arb.char().filter { it in 'A'..'Z' },
            Arb.int(0..100),
            Arb.int(0..100)
        ) { letter, a, b ->
            val step1 = cipher.cipher(letter, a)
            val step2 = cipher.cipher(step1, b)
            val combined = cipher.cipher(letter, (a + b) % 26)
            step2 shouldBe combined
        }
        println("Test OK : composition des décalages")
    }
    /*test("test de chiffrement manuel avec erreur") {
        val letter = 'A'
        val expected = listOf('A', 'B', 'C', 'D', 'E', 'F')

        for (shift in 0..5) {
            val result = cipher.cipher(letter, shift)
            println("Décalage $shift : '$letter' -> '$result'")
            // Provoque une erreur volontaire sur le décalage 3
            if (shift == 3) {
                result shouldBe 'Z' // 💥 faux exprès
            } else {
                result shouldBe expected[shift]
            }
        }
    }*/
})
