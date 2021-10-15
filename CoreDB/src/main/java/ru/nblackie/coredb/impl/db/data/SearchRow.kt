package ru.nblackie.coredb.impl.db.data

/**
 * @author Ilya Tatarchuk
 */
data class SearchRow(
    val id: Int,
    val word: String,
    val transcription: String? = null,
    val translation: String
)