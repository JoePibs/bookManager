package bookManager.domain.model

data class Book(
    val title: String,
    val author: String,
    val isReserved: Boolean = false
) {
    init {
        require(title.isNotBlank()) { "Book title must not be blank" }
        require(author.isNotBlank()) { "Book author must not be blank" }
    }
}
