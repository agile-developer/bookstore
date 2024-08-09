package co.bound.exercise.bookstore

import co.bound.exercise.thirdparties.boogle.BoogleClient
import co.bound.exercise.thirdparties.valdivia.ValdiviaClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class BookstoreApplication {

	@Bean
	fun boogleClient(): BoogleClient {
		return BoogleClient()
	}

	@Bean
	fun valdiviaClient(): ValdiviaClient {
		return ValdiviaClient()
	}
}

fun main(args: Array<String>) {
	runApplication<BookstoreApplication>(*args)
}
