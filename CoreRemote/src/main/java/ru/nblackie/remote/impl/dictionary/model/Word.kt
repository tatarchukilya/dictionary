package ru.tatarchuk.personaldictionary.data.remote.rest.dictionary.model

    data class Word(
    val transcription: String,
    val translation: List<String>,
    val word: String
)