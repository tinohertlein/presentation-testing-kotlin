package dev.hertlein.testing.starwars

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Almanach")
internal class AlmanachTestV1 {

    private val webClient = mockk<StarWarsApiClient>()
    private val almanach = Almanach(webClient)

    @BeforeEach
    fun beforeEach() {
        every { webClient.fetchPlanets() } returns emptyList()
        every { webClient.fetchCharacters() } returns emptyList()
    }

    @Nested
    inner class FetchCharacters {

        @Test
        fun `should return empty list if web client returns no characters`() {
            every { webClient.fetchCharacters() } returns emptyList()

            val characters = almanach.fetchCharacters()

            assertThat(characters).isEmpty()
            verify { webClient.fetchCharacters() }
            verify { webClient.fetchPlanets() }
        }

        @Test
        fun `should associate character and planet if web client returns character and its homeworld`() {
            every { webClient.fetchPlanets() } returns listOf(TestFixtures.Dtos.TATOOINE)
            every { webClient.fetchCharacters() } returns listOf(TestFixtures.Dtos.LUKE)

            val characters = almanach.fetchCharacters()

            assertThat(characters).containsExactly(TestFixtures.Entities.LUKE)
            verify { webClient.fetchCharacters() }
            verify { webClient.fetchPlanets() }
        }
    }

    @Nested
    inner class FetchPlanets {

        @Test
        fun `should return empty list if web client returns no planets`() {
            every { webClient.fetchPlanets() } returns emptyList()

            val planets = almanach.fetchPlanets()

            assertThat(planets).isEmpty()
            verify { webClient.fetchCharacters() }
            verify { webClient.fetchPlanets() }
        }

        @Test
        fun `should associate planet and character if web client returns planet and its inhabitant`() {
            every { webClient.fetchPlanets() } returns listOf(TestFixtures.Dtos.TATOOINE)
            every { webClient.fetchCharacters() } returns listOf(
                TestFixtures.Dtos.LUKE,
                TestFixtures.Dtos.DARTH
            )

            val planets = almanach.fetchPlanets()

            assertThat(planets).containsExactly(TestFixtures.Entities.TATOOINE)
            verify { webClient.fetchCharacters() }
            verify { webClient.fetchPlanets() }
        }
    }
}
