package bookManager

class Cipher {
    fun cipher(letter: Char, number: Int): Char {
        require(letter in 'A'..'Z') { "Seules les lettres majuscules sont autorisées" }
        require(number >= 0) { "La clé ne peut pas être négative" }

        val alphabetLimit = number % 26
        val letterValue = letter.code + alphabetLimit // generer erreur sur les tests ciffrer +1
        return if (letterValue > 'Z'.code) {
            (letterValue - 26).toChar()
        } else {
            letterValue.toChar()
        }
    }

    fun decipher(letter: Char, number: Int): Char {
        require(letter in 'A'..'Z') { "Seules les lettres majuscules sont autorisées" }
        require(number >= 0) { "La clé ne peut pas être négative" }

        val alphabetLimit = number % 26
        val letterValue = letter.code - alphabetLimit
        return if (letterValue < 'A'.code) {
            (letterValue + 26).toChar()
        } else {
            letterValue.toChar()
        }
    }


}
