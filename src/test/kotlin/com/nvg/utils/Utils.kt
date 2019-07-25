package com.nvg.utils

import io.restassured.response.ResponseBodyExtractionOptions
import io.restassured.specification.RequestSpecification

fun RequestSpecification.When() = this.`when`()

fun loadFile(filename: String) = ClassLoader.getSystemResource(filename)

inline fun <reified T> ResponseBodyExtractionOptions.to(): T {
    return this.`as`(T::class.java)
}