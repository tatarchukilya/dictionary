package ru.nblackie.dictionary.impl.domain.converter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.data.model.Translation
import ru.nblackie.dictionary.impl.domain.model.SearchItem

/**
 * @author tatarchukilya@gmail.com
 */

internal fun SearchResult.toItem(): SearchItem {
    return SearchItem(word, transcription ?: "", translation, translation.joinToString { it.data })
}

internal fun List<Translation>.toSpannable(resourceManager: ResourceManager): SpannableString {
    val string = joinToString { it.data }
    var start = 0
    var end: Int
    val spannable = SpannableString(string)
    forEach {
        if (it.isAdded) {
            end = start + it.data.length
            spannable.setSpan(
                ForegroundColorSpan(resourceManager.getColor(R.color.lime)),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                StyleSpan(Typeface.BOLD_ITALIC),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            start = end + 2
        } else {
            return@forEach
        }
    }
    return spannable
}

internal fun SearchResult.toSearchSpannableItem(resourceManager: ResourceManager): SearchItem {
    return SearchItem(word, transcription ?: "", translation, translation.toSpannable(resourceManager))
}

