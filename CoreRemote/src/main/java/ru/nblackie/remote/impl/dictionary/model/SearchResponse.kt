package ru.nblackie.remote.impl.dictionary.model

data class SearchResponse(
    val result: List<SearchResultRest>,
    val status: Int
)