package com.nvg

import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

class `Sample RestAssured Test Suite`: TestBase() {

    @Test
    fun `Body name should be this`() {
        given()
            .`when`()
                .get("/something")
            .then()
                .body("name", `is`("Toutes les rubriques"))
    }

    @Test
    fun `Same test but with real api`() {
        given().baseUri("https://api-stage.anibis.ch/v1")
            .port(8080)
            .`when`()
                .get("/fr/categories")
            .then()
                .body("name", `is`("Toutes les rubriques"))
    }

    @Test
    fun `this test will spam Khoi's mailbox`() {
        given().baseUri("https://xbapi-stage.anibis.ch/v1")
            .port(8080)
            .header("accept", "application/json", "Content-Type", "application/json")
            .body("{ \"name\": \"Test\", \"email\": \"khoivo@nvg.vn\", \"message\": \"release\", \"copy\": true}")
            .auth().basic("reseller.nvg@gmail.com", "nvg07072012")
            .`when`()
            .post("/fr/listings/24311110/contact")
            .then()
            .statusCode(200)
    }
}
