package com.nvg.test

import com.nvg.model.SearchResults
import com.nvg.utils.When
import com.nvg.utils.loadFile
import com.nvg.utils.to
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SampleTest: TestBase() {

    @Test
    fun `Body name should be this`() {
        given()
            .When()
                .get("/something")
            .then()
                .body("name", `is`("Toutes les rubriques"))
    }

    @Test
    fun `Same test but with real api`() {
        given().baseUri("https://api-stage.anibis.ch/v1")
            .port(8080)
            .When()
                .get("/fr/categories")
            .then()
                .body("name", `is`("Toutes les rubriques"))
    }

    @Test
    fun `Post request to a useless endpoint`() {
        given().baseUri("https://xbapi-stage.anibis.ch/v1")
            .port(8080)
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(String.format("{\"SearchText\":\"%s\",\"ResultRows\":%s,\"ResultStart\":%s}", "QA Test", 25, 0))
            .When()
                .post("/Search")
            .then()
                .statusCode(200)
                .assertThat()
                    .body(matchesJsonSchema(loadFile("schemas/search.json")))
    }

    @Test
    fun `Deserialize successful`() {
        val jsonObj = given().baseUri("https://xbapi-stage.anibis.ch/v1")
            .port(8080)
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(String.format("{\"SearchText\":\"%s\",\"ResultRows\":%s,\"ResultStart\":%s}", "QA Test", 25, 0))
            .When()
            .post("/Search")
            .then().extract().to<SearchResults>()

        assertThat(jsonObj.ObjectIds, not(emptyList()))
    }

    @Test
    fun `test exception is thrown when wrong thing happened`() {
        exception.expect(AssertionError::class.java)
        exception.expectMessage(containsString("Expected content-type \"XML\" doesn't match actual content-type \"application/json; charset=utf-8\"."))

        given().baseUri("https://api-stage.anibis.ch/v1")
            .port(8080)
            .When()
            .get("/fr/categories")
            .then()
            .contentType(ContentType.XML)
    }

    @Test
    fun `Authorization failed`() {
        given().baseUri("https://api.immoscout24.ch/v1")
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
        given().baseUri("https://api.immoscout24.ch/v1")
            .port(8080)
            .headers(is24Headers)
            .header("Accept-Language", "en")
            .auth().basic("huyhua@nvg.vn", "123456")
            .When()
                .get("/private/users/login")
            .then()
                .statusCode(200)
    }

    @Test
    fun `Return khoivo relationships`() {
        given()
            .When()
                .get("/khoivo")
            .then()
                .statusCode(200)
            .and()
                .assertThat().body(matchesJsonSchema(loadFile("schemas/khoivo.json")))
    }
}
