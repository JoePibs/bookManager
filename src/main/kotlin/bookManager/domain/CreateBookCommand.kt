package bookManager.domain

data class CreateBookCommand(
    val title: String,
    val author: String
)
