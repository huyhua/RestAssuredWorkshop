package com.nvg.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.nvg.utils.When
import com.nvg.utils.loadFile
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.lanwen.wiremock.ext.WiremockResolver
import ru.lanwen.wiremock.ext.WiremockResolver.*
import ru.lanwen.wiremock.ext.WiremockUriResolver
import ru.lanwen.wiremock.ext.WiremockUriResolver.*
import java.lang.AssertionError

@ExtendWith(
    WiremockResolver::class,
    WiremockUriResolver::class
)
class MockedTest: TestBase() {

    @BeforeEach
    fun setup(@Wiremock wiremock: WireMockServer, @WiremockUri uri: String) {
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri(uri)
            .build()
    }

    @Test
    fun `Body name should be this`() {
        given()
            .When()
                .get("/something")
            .then()
                .body("name", `is`("Toutes les rubriques"))
    }

    @Test
    fun `test exception is thrown when wrong thing happened`() {
        assertThrows<AssertionError> {
            given().baseUri("https://api-stage.anibis.ch/v1")
                .port(8080)
                .When()
                .get("/fr/categories")
                .then()
                .contentType(ContentType.XML)
        }
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
