package ru.nblackie.dictionary.impl.domain.model

import android.text.Spannable
import ru.nblackie.dictionary.impl.data.model.Translation

/**
 * @author Ilya Tatarchuk
 */
internal data class SearchSpannableItem(
    val word: String,
    val transcription: String,
    val translation: List<Translation>,
    val spannable: Spannable,
    override val type: ItemType = ItemType.SEARCH_SPAN
) : TypedItem