package ru.nblackie.dictionary.impl.data.model

/**
 * @author Ilya Tatarchuk
 */
internal data class NewTranslation(
    val word: String,
    val transcription: String? = null,
    val translation: String
)
