package ru.nblackie.coredb.impl.db.data

/**
 * @author Ilya Tatarchuk
 */
data class WordData(
    val word: WordLocal,
    val transcription: String,
    val translation: List<WordLocal>
)
