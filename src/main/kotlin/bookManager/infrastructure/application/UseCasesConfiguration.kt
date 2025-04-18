package bookManager.infrastructure.application

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import bookManager.domain.port.BookRepository
import bookManager.application.CreateBookUseCase
import bookManager.application.ListBooksUseCase

@Configuration
class UseCasesConfiguration {

    @Bean
    fun createBookUseCase(bookRepository: BookRepository): CreateBookUseCase =
        CreateBookUseCase(bookRepository)

    @Bean
    fun listBooksUseCase(bookRepository: BookRepository): ListBooksUseCase =
        ListBooksUseCase(bookRepository)
}
