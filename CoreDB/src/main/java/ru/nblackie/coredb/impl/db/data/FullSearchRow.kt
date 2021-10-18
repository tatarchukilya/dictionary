package ru.nblackie.coredb.impl.db.data

/**
 * @author Ilya Tatarchuk
 */
data class FullSearchRow(
    val id: Int,
    val word: String,
    val transcription: String? = null,
    val translation: String
)