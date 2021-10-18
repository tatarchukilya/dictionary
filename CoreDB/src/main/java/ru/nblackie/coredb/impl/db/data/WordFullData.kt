package ru.nblackie.coredb.impl.db.data

/**
 * @author Ilya Tatarchuk
 */
data class WordFullData(
    val word: WordLocal,
    val transcription: String? = null,
    val translation: List<WordLocal>
)
