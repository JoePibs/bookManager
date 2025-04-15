package bookManager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication {

    @Bean
    fun run(): CommandLineRunner {
        return CommandLineRunner {
            val result = Cipher().cipher('B', 28)
            println("Résultat du chiffrement de 'A' avec la clé 5 : $result")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
