package com.nvg.model

data class SearchResults (
    var TotalCount: Int,
    var ResultRows: Int,
    var ResultStart: Int,
    var ObjectIds: List<Int>?
)