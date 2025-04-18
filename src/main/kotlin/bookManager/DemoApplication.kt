package bookManager

import bookManager.domain.model.Book
import bookManager.domain.usecase.BookService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication {

    @Bean
    fun run(bookService: BookService): CommandLineRunner {
        return CommandLineRunner {
            val book = Book(title = "Pney", author = "Plouf")
            bookService.addBook(book)
            println("Livre ajouté : ${book.title} de ${book.author}")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
