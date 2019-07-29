package com.nvg.test

import com.nvg.model.SearchResults
import com.nvg.utils.When
import com.nvg.utils.loadFile
import com.nvg.utils.to
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.module.jsv.JsonSchemaValidator
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        RestAssured.given().baseUri("https://api-stage.anibis.ch/v1")
            .When()
            .get("/fr/categories")
            .then()
            .body("name", CoreMatchers.`is`("Toutes les rubriques"))
    }

    @Test
    fun `Post request to a useless endpoint`() {
        RestAssured.given()
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
        RestAssured.given()
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
        RestAssured.given().baseUri("https://api.immoscout24.ch/v1")
            .port(8080)
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
        RestAssured.given().baseUri("https://api.immoscout24.ch/v1")
            .port(8080)
            .headers(is24Headers)
            .header("Accept-Language", "en")
            .auth().basic("huyhua@nvg.vn", "123456")
            .When()
            .get("/private/users/login")
            .then()
            .statusCode(200)
    }
}