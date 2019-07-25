package com.nvg.utils

import io.restassured.specification.RequestSpecification

fun RequestSpecification.When() = this.`when`()

fun loadFile(filename: String) = ClassLoader.getSystemResource(filename)