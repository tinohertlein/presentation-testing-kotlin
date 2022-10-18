package dev.hertlein.testing.starwars

import dev.hertlein.testing.starwars.StarWarsApiClient.CharacterDto
import dev.hertlein.testing.starwars.StarWarsApiClient.PlanetDto

internal object TestFixtures {

    object Entities {
        val LUKE = Character("Luke Skywalker", "Tatooine")
        val TATOOINE = Planet("Tatooine", listOf("Luke Skywalker", "Darth Vader"))
    }

    object Dtos {
        val LUKE = CharacterDto(
            "Luke Skywalker",
            "https://swapi.dev/api/planets/1/"
        )
        val R2D2 = CharacterDto("R2-D2", "https://swapi.dev/api/planets/8/")
        val DARTH = CharacterDto(
            "Darth Vader",
            "https://swapi.dev/api/planets/1/"
        )

        val TATOOINE = PlanetDto(
            "Tatooine",
            "https://swapi.dev/api/planets/1/"
        )
        val NABOO = PlanetDto("Naboo", "https://swapi.dev/api/planets/8/")
    }
}
