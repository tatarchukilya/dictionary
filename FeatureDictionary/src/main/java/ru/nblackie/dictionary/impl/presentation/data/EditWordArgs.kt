package ru.nblackie.dictionary.impl.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author tatarchukilya@gmail.com
 */
@Parcelize
internal data class EditWordArgs(val title: String, val content: List<String>) : Parcelable
