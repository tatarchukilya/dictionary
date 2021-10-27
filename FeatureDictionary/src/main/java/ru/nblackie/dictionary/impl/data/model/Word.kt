package ru.nblackie.dictionary.impl.data.model

/**
 * @author Ilya Tatarchuk
 */
internal data class Word(
    val data: String,
    val transcription: String?,
    val translations: List<String>
)