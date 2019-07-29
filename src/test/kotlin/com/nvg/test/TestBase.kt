package com.nvg.test

import com.nvg.utils.IS24Auth
import io.restassured.http.Header
import io.restassured.http.Headers
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.CONCURRENT)
abstract class TestBase {

    protected val is24Headers by lazy {
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