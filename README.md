# Presentation: Testing with Kotlin

![tests](https://github.com/tinohertlein/presentation-testing-kotlin/actions/workflows/master.yml/badge.svg?event=push)

This repository contains a presentation and accompanying examples that demonstrate some techniques, how to write tests that "talk" to you.
In addition, there is a very brief code-comparison between the usage
of [native Junit assertions](https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-assertions)
and [AssertJ](https://assertj.github.io/doc/) on the one hand and between [Mockito](https://github.com/mockito/mockito)
and [mockk](https://mockk.io/) as mocking libraries for Kotlin on the other hand.

The application that is used as the subject under test is a very
simple [StarWars-Almanach](src/main/kotlin/dev/hertlein/testing/starwars/Almanach.kt): the Almanach fetches the planets
and characters from the [Star Wars API](https://swapi.dev/) and outputs the characters mapped to their homeworld or the
planets showing their most famous (former) inhabitants.

The presentation is made with [reveal.js](https://revealjs.com/) and can be viewed by opening [index.html](https://github.com/tinohertlein/presentation-testing-kotlin/blob/master/docs/presentation/index.html) in a browser.
