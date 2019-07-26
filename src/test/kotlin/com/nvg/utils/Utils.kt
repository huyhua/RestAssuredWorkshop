package com.nvg.utils

import io.restassured.response.ResponseBodyExtractionOptions
import io.restassured.specification.RequestSpecification
import java.net.URL

fun RequestSpecification.When(): RequestSpecification = this.`when`()

fun loadFile(filename: String): URL = ClassLoader.getSystemResource(filename)

inline fun <reified T> ResponseBodyExtractionOptions.to(): T = this.`as`(T::class.java)