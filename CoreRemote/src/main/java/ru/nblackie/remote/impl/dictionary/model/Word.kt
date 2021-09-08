package ru.nblackie.remote.impl.dictionary.model

data class Word(
    val word: String,
    val transcription: String,
    val translation: List<String>,
)