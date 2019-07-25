package com.nvg

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.Before
import org.junit.Rule

abstract class TestBase {
    @Rule
    @JvmField
    val wireMockRule = WireMockRule(options().dynamicPort())

    @Before
    fun setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        RestAssured.requestSpecification = RequestSpecBuilder()
            .setBaseUri(wireMockRule.baseUrl())
            .setPort(wireMockRule.port())
            .build()
    }
}