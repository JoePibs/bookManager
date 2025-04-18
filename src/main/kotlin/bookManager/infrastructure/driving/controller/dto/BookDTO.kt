package bookManager.infrastructure.driving.controller.dto

import bookManager.domain.model.Book
import bookManager.domain.CreateBookCommand

data class BookDTO(
    val title: String,
    val author: String
)

fun BookDTO.toCommand(): CreateBookCommand =
    CreateBookCommand(title = title, author = author)

fun Book.toDTO(): BookDTO =
    BookDTO(title = this.title, author = this.author)
