package ru.nblackie.dictionary.impl.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author tatarchukilya@gmail.com
 */
@Parcelize
data class WordArgs(
    val word: String,
    val transcription: String,
    val translation: List<String>
) : Parcelable