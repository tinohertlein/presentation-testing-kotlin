package dev.hertlein.testing.starwars

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import dev.hertlein.testing.starwars.StarWarsApiClient.EntityType.CHARACTER
import dev.hertlein.testing.starwars.StarWarsApiClient.EntityType.PLANET
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


internal class StarWarsApiClient(
    private val client: HttpClient = HttpClient.newBuilder().build(),
    private val jsonParser: Json = Json { ignoreUnknownKeys = true }
) {

    companion object {
        private const val CHARACTERS_START_URL = "https://swapi.dev/api/people"
        private const val PLANETS_START_URL = "https://swapi.dev/api/planets"
    }

    enum class EntityType {
        CHARACTER, PLANET
    }

    data class CharacterDto(val name: String, val homeworldUrl: String)

    data class PlanetDto(val name: String, val url: String)

    @Serializable
    private data class ApiResponse(val next: String?, @SerialName("results") val dtos: List<Dto>) {
        @Serializable
        data class Dto(val name: String, val url: String, val homeworld: String? = null)
    }

    private val entityCache: LoadingCache<EntityType, List<Any>> = CacheBuilder.newBuilder()
        .build(
            object : CacheLoader<EntityType, List<Any>>() {
                override fun load(entityType: EntityType): List<Any> {
                    return when (entityType) {
                        CHARACTER -> fetchCharacters(CHARACTERS_START_URL, emptyList())
                        PLANET -> fetchPlanets(PLANETS_START_URL, emptyList())
                    }
                }
            })

    fun fetchCharacters(): List<CharacterDto> = entityCache.get(CHARACTER).filterIsInstance(CharacterDto::class.java)

    fun fetchPlanets(): List<PlanetDto> = entityCache.get(PLANET).filterIsInstance(PlanetDto::class.java)

    private fun fetchCharacters(
        url: String?,
        characters: List<CharacterDto>
    ): List<CharacterDto> {
        if (url == null) {
            return characters
        }
        println("Fetching characters from $url...")
        val json = fetchEntityPage(url)

        return fetchCharacters(
            json.next,
            characters + json.dtos.map { CharacterDto(it.name, it.homeworld ?: UNKNOWN) })
    }

    private fun fetchPlanets(
        url: String?,
        planets: List<PlanetDto>
    ): List<PlanetDto> {
        if (url == null) {
            return planets
        }
        println("Fetching planets from $url...")
        val json = fetchEntityPage(url)

        return fetchPlanets(json.next, planets + json.dtos.map { PlanetDto(it.name, it.url) })
    }

    private fun fetchEntityPage(url: String): ApiResponse =
        url.let {
            val request = HttpRequest.newBuilder().uri(URI.create(it)).build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            jsonParser.decodeFromString(response.body())
        }
}
