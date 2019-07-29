package com.nvg.test

import com.nvg.model.SearchResults
import com.nvg.utils.IS24Auth
import com.nvg.utils.When
import com.nvg.utils.loadFile
import com.nvg.utils.to
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.http.Header
import io.restassured.http.Headers
import io.restassured.module.jsv.JsonSchemaValidator
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.AssertionError

class NoMockTest: TestBase() {

    @BeforeEach
    fun setup() {
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri("https://xbapi-stage.anibis.ch/v1")
            .setPort(8080)
            .build()
    }

    @Test
    fun `Same test but with real api`() {
        given()
            .baseUri("https://api-stage.anibis.ch/v1")
        .When()
            .get("/fr/categories")
        .then()
            .body("name", CoreMatchers.`is`("Toutes les rubriques"))
    }

    @Test
    fun `Post request to a useless endpoint`() {
        given()
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(String.format("{\"SearchText\":\"%s\",\"ResultRows\":%s,\"ResultStart\":%s}", "QA Test", 25, 0))
        .When()
            .post("/Search")
            .then()
            .statusCode(200)
            .body(JsonSchemaValidator.matchesJsonSchema(loadFile("schemas/search.json")))
    }

    @Test
    fun `Deserialize successful`() {
        given()
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(String.format("{\"SearchText\":\"%s\",\"ResultRows\":%s,\"ResultStart\":%s}", "QA Test", 25, 0))
        .When()
            .post("/Search")
        .then()
            .extract().to<SearchResults>()
            .apply { MatcherAssert.assertThat(this.ObjectIds, CoreMatchers.not(emptyList())) }
    }

    @Test
    fun `Authorization failed`() {
        given()
            .baseUri("https://api.immoscout24.ch/v1")
            .headers(is24Headers)
            .header("Accept-Language", "en")
            .auth().basic("huyhua@nvg.vn", "1233") // Wrong password
        .When()
            .get("/private/users/login")
        .then()
            .statusCode(401)
    }

    @Test
    fun `Authorization passed`() {
        given()
            .baseUri("https://api.immoscout24.ch/v1")
            .headers(is24Headers)
            .header("Accept-Language", "en")
            .auth().basic("huyhua@nvg.vn", "123456")
        .When()
            .get("/private/users/login")
        .then()
            .statusCode(200)
    }

    @Test
    fun `test exception is thrown when wrong thing happened`() {
        assertThrows<AssertionError> {
            given()
                .baseUri("https://api-stage.anibis.ch/v1")
                .port(8080)
            .When()
                .get("/fr/categories")
            .then()
                .contentType(ContentType.XML)
        }
    }

    private val is24Headers by lazy {
        IS24Auth().run {
            Headers(
                Header("X-Request-Date", this.date),
                Header("X-Udid", this.udid),
                Header("X-Token", this.token),
                Header("X-IS24-Api-Key", this.apiKey)
            )
        }
    }
}