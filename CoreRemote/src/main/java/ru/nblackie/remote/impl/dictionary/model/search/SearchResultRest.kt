package ru.nblackie.remote.impl.dictionary.model.search

data class SearchResultRest(
    val id: Int,
    val word: String,
    val transcription: String?,
    val translation: List<String>,
)