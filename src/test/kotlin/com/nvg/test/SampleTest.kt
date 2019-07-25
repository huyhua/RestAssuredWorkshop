package com.nvg.test

import com.nvg.utils.When
import com.nvg.utils.loadFile
import io.restassured.RestAssured.given
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema
import org.hamcrest.CoreMatchers.`is`
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
}
