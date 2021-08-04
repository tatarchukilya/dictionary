package ru.tatarchuk.personaldictionary.data.remote.rest.dictionary.model

import com.google.gson.annotations.SerializedName

/**
 * @author tatarchukilya@gmail.com
 */
data class NewWord(
    @SerializedName("name")
    val name: String,
    @SerializedName("transcription")
    val transcription: String,
    @SerializedName("translation")
    val translation: List<String>,
    @SerializedName("example")
    val example: List<String>
)
