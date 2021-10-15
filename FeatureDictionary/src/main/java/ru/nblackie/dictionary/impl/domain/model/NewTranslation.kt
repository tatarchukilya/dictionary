package ru.nblackie.dictionary.impl.domain.model

/**
 * @author Ilya Tatarchuk
 */
data class NewTranslation(
    val word: String,
    val transcription: String,
    val translation: String
)