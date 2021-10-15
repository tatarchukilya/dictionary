package ru.nblackie.dictionary.impl.data.model

/**
 * @author Ilya Tatarchuk
 */
data class SearchResult(
    val word: String,
    val transcription: String?,
    val translation: List<String>
)