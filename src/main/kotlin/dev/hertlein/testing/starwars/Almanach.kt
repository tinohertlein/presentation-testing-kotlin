package dev.hertlein.testing.starwars

import com.github.freva.asciitable.AsciiTable
import com.github.freva.asciitable.Column

const val UNKNOWN = "unknown"

fun main() {
    val almanach = Almanach(StarWarsApiClient())

    val charactersOutput = AsciiTable.getTable(almanach.fetchCharacters(),
        listOf(
            Column().header("Character").with { it.name },
            Column().header("Homeworld").with { it.homeworld }
        )
    )

    val planetsOutput = AsciiTable.getTable(almanach.fetchPlanets(),
        listOf(
            Column().header("Planet").with { it.name },
            Column().header("# being homeworld").with { it.homeworldOf.size.toString() },
            Column().header("Homeworld of").with { it.homeworldOf.joinToString() }
        )
    )

    println(charactersOutput)
    println(planetsOutput)
}

internal class Almanach(private val client: StarWarsApiClient) {

    fun fetchCharacters(): List<Character> {
        val planets = client.fetchPlanets()
        val characters = client.fetchCharacters()

        return characters.map { character ->
            val homeworld = planets.find { planet -> planet.url == character.homeworldUrl }
            Character(character.name, homeworld?.name ?: UNKNOWN)
        }
    }

    fun fetchPlanets(): List<Planet> {
        val planets = client.fetchPlanets()
        val characters = client.fetchCharacters()

        return planets.map { planet ->
            val homeworldOf = characters
                .filter { character -> character.homeworldUrl == planet.url }
                .map { it.name }
            Planet(planet.name, homeworldOf)
        }.sortedByDescending { it.homeworldOf.size }
    }
}
