package ru.nblackie.remote.impl.dictionary.model.search

data class SearchResponse(
    val result: List<RemoteSearchResult>,
    val status: Int
)