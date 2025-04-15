package bookManager.domain.model

data class Book(val title: String, val author: String) {
    init {
        require(title.isNotBlank()) { "Book title must not be blank" }
        require(author.isNotBlank()) { "Book author must not be blank" }
    }
}