package anthill.Anthill

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AnthillApplication

fun main(args: Array<String>) {
    SpringApplication.run(AnthillApplication::class.java, *args)
}

