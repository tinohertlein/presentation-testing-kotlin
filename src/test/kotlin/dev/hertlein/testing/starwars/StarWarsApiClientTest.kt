package dev.hertlein.testing.starwars

import com.google.common.io.Resources
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

@DisplayName("StarWarsApiClient")
internal class StarWarsApiClientTest {

    private val httpClient = mockk<HttpClient>()
    private val starWarsApiClient = StarWarsApiClient(httpClient)

    @BeforeEach
    fun beforeEach() {
        mapOf(
            "https://swapi.dev/api/people" to "people_page_1.json",
            "https://swapi.dev/api/people/?page=2" to "people_page_2.json",
            "https://swapi.dev/api/planets" to "planets_page_1.json",
            "https://swapi.dev/api/planets/?page=2" to "planets_page_2.json",
        )
            .forEach { (requestUrl, fileContainingResponse) ->
                val request = HttpRequest.newBuilder().uri(URI.create(requestUrl)).build()
                val response = mockk<HttpResponse<String>>()

                every { response.body() } returns Resources.toString(
                    Resources.getResource(fileContainingResponse), StandardCharsets.UTF_8
                )
                every<HttpResponse<String>?> { httpClient.send(request, any()) } returns response
            }
    }

    @Nested
    inner class FetchCharacters {

        @Test
        fun `should fetch characters if they are distributed among multiple pages`() {
            val characters = starWarsApiClient.fetchCharacters()

            assertThat(characters).containsExactly(
                TestFixtures.Dtos.LUKE,
                TestFixtures.Dtos.R2D2
            )
        }
    }

    @Nested
    inner class FetchPlanets {

        @Test
        fun `should fetch planets if they are distributed among multiple pages`() {
            val planets = starWarsApiClient.fetchPlanets()

            assertThat(planets).containsExactly(
                TestFixtures.Dtos.TATOOINE,
                TestFixtures.Dtos.NABOO,
            )
        }
    }
}
