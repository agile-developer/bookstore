package co.bound.exercise.bookstore

import co.bound.exercise.bookstore.catalog.controller.SearchField
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class BookstoreApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

//	@Test
//	fun contextLoads() {
//	}

    @Test
    fun `should return a list of books when searching by author`() {
        // arrange
        val author = "Gabriel Garcia Marquez"

        // act & assert
        mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/catalog")
                .queryParam("value", author)
                .queryParam("field", SearchField.AUTHOR.name)
                .accept(APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.books").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.books[0].title").value("One Hundred Years of Solitude"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.books[1].title").value("Chronicle of a Death Foretold"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return an empty list when searching for an unknown author`() {
        // arrange
        val author = "Unknown"

        // act & assert
        mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/catalog")
                .queryParam("value", author)
                .queryParam("field", SearchField.AUTHOR.name)
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.books").isEmpty)
    }

    @Test
    fun `should return a quote for an ISBN`() {
        // arrange
        val isbn = "978-0141184999"

        // act & assert
        mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/quote")
                .queryParam("isbn", isbn)
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price").exists())
    }

    @Test
    fun `should create an order for a valid quoteId`() {
        // arrange
        val isbn = "978-0141184999"
        val quoteResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/quote")
                .queryParam("isbn", isbn)
                .accept(APPLICATION_JSON)
        ).andReturn()
        val quoteJson = quoteResult.response.contentAsString
        val quoteId = JsonPath.read<String>(quoteJson, "$.id")
        val createOrderRequest = """
			{
				"idempotencyId": "${UUID.randomUUID()}",
				"quoteId": "$quoteId",
				"deliveryAddress": {
					"addressLine1": "Wendover Court",
					"addressLine2": "Hendon Way",
					"postcode": "NW2 2PD",
					"city": "London",
					"country": "United Kingdom"
				}
			}
		""".trimIndent()

        // act & assert
        mockMvc.perform(
            MockMvcRequestBuilders.post("/bookstore/order")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(createOrderRequest)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.quoteId").value(quoteId))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CREATED"))
    }

    @Test
    fun `should return correct statistics for book after searching and ordering`() {
        // arrange
        val author = "Katie Mack"

        // Search twice
        mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/catalog")
                .queryParam("value", author)
                .queryParam("field", SearchField.AUTHOR.name)
                .accept(APPLICATION_JSON)
        )
        val searchResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/catalog")
                .queryParam("value", author)
                .queryParam("field", SearchField.AUTHOR.name)
                .accept(APPLICATION_JSON)
        ).andReturn()
        val searchJson = searchResult.response.contentAsString
        val isbn = JsonPath.read<String>(searchJson, "$.books[0].isbn")

        // Create quote
        val quoteResult = mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/quote")
                .queryParam("isbn", isbn)
                .accept(APPLICATION_JSON)
        ).andReturn()
        val quoteJson = quoteResult.response.contentAsString
        val quoteId = JsonPath.read<String>(quoteJson, "$.id")

        // Create order
        val createOrderRequest = """
			{
				"idempotencyId": "${UUID.randomUUID()}",
				"quoteId": "$quoteId",
				"deliveryAddress": {
					"addressLine1": "Wendover Court",
					"addressLine2": "Hendon Way",
					"postcode": "NW2 2PD",
					"city": "London",
					"country": "United Kingdom"
				}
			}
		""".trimIndent()
        mockMvc.perform(
            MockMvcRequestBuilders.post("/bookstore/order")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(createOrderRequest)
        )

        // act & assert
        mockMvc.perform(
            MockMvcRequestBuilders.get("/bookstore/statistics")
                .queryParam("isbn", isbn)
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn))
            .andExpect(MockMvcResultMatchers.jsonPath("$.searchCount").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.orderCount").value(1))
    }
}
