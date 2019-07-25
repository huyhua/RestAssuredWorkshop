package com.nvg.test

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.nvg.utils.IS24Auth
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.Header
import io.restassured.http.Headers
import org.junit.Before
import org.junit.Rule
import org.junit.rules.ExpectedException



abstract class TestBase {
    @Rule
    @JvmField
    val wireMockRule = WireMockRule(options().dynamicPort())

    @Rule
    @JvmField
    var exception: ExpectedException = ExpectedException.none()

    @Before
    fun setup() {
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri(wireMockRule.baseUrl())
            .setPort(wireMockRule.port())
            .build()
    }

    protected val is24Headers by lazy {
        IS24Auth().run {
            Headers(
                Header("X-Request-Date", this.date),
                Header("X-Udid", this.UDID),
                Header("X-Token", this.token),
                Header("X-IS24-Api-Key", this.apiKey)
            )
        }
    }
}