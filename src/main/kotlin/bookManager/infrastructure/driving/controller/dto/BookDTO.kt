package bookManager.infrastructure.driving.controller.dto

import bookManager.domain.model.Book
import bookManager.domain.CreateBookCommand

data class BookDTO(
    val title: String,
    val author: String,
    val isReserved: Boolean = false
)

fun BookDTO.toCommand(): CreateBookCommand =
    CreateBookCommand(title = title, author = author, is_reserved = isReserved)

fun Book.toDTO(): BookDTO =
    BookDTO(title = this.title, author = this.author, isReserved = this.isReserved)

