package dev.hertlein.testing.starwars

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

internal class AlmanachTestV0 {

    private val webClient = Mockito.mock(StarWarsApiClient::class.java)
    private val almanach = Almanach(webClient)

    @BeforeEach
    fun beforeEach() {
        `when`(webClient.fetchCharacters()).thenReturn(emptyList())
        `when`(webClient.fetchPlanets()).thenReturn(emptyList())
    }

    @Test
    fun `fetchCharacters should return empty list if web client returns no characters`() {
        `when`(webClient.fetchCharacters()).thenReturn(emptyList())

        val characters = almanach.fetchCharacters()

        assertTrue { characters.isEmpty() }
        verify(webClient).fetchPlanets()
        verify(webClient).fetchCharacters()
    }

    @Test
    fun `fetchCharacters should associate character and planet if web client returns character and its homeworld`() {
        `when`(webClient.fetchPlanets()).thenReturn(listOf(TestFixtures.Dtos.TATOOINE))
        `when`(webClient.fetchCharacters()).thenReturn(listOf(TestFixtures.Dtos.LUKE))

        val characters = almanach.fetchCharacters()

        assertEquals(listOf(TestFixtures.Entities.LUKE), characters)
        verify(webClient).fetchPlanets()
        verify(webClient).fetchCharacters()
    }
    @Test
    fun `fetchPlanets should return empty list if web client returns no planets`() {
        `when`(webClient.fetchPlanets()).thenReturn(emptyList())

        val planets = almanach.fetchPlanets()

        assertTrue { planets.isEmpty() }
        verify(webClient).fetchPlanets()
        verify(webClient).fetchCharacters()
    }
    @Test
    fun `fetchPlanets should associate planet and character if web client returns planet and its inhabitant`() {
        `when`(webClient.fetchPlanets()).thenReturn(listOf(TestFixtures.Dtos.TATOOINE))
        `when`(webClient.fetchCharacters()).thenReturn(listOf(TestFixtures.Dtos.LUKE, TestFixtures.Dtos.DARTH))

        val planets = almanach.fetchPlanets()

        assertEquals(listOf(TestFixtures.Entities.TATOOINE), planets)
        verify(webClient).fetchPlanets()
        verify(webClient).fetchCharacters()
    }
}
