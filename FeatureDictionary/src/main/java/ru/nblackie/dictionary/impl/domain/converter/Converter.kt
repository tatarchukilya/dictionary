package ru.nblackie.dictionary.impl.domain.converter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.model.Translation
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.dictionary.impl.domain.model.SearchItem

/**
 * @author tatarchukilya@gmail.com
 */

/**
 * Преобразует список слов в строку, выделят в полученной строке слова, которые есть в БД
 *
 * @param resourceManager доступ к ресурсам, нужен, чтобы получить цвет для [ForegroundColorSpan]
 */
//TODO возможность получать ресурсы с поправкой на тему
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
            start = end + 2 // Пропустить пробел с запятой
        } else {
            return@forEach
        }
    }
    return spannable
}

/**
 * Возвращает [SearchItem] из двух [Word]. Предполагается, что [term] получен из БД, и все варианты перевода,
 * которые он содержит меняют [Translation.isAdded] на true
 *
 * @param term - данные из БД
 */
internal fun Word.concat(term: Word?, resourceManager: ResourceManager): SearchItem {
    val concatTranslation = (translations + (term?.translations ?: listOf()))
        .groupBy { it }
        .map { Translation(it.key, term?.translations?.find { tr -> tr == it.key } != null) }
        .sortedBy { !it.isAdded }
    return SearchItem(
        data,
        transcription ?: term?.transcription ?: "[]",
        concatTranslation,
        concatTranslation.toSpannable(resourceManager)
    )
}

internal fun Word.toSearchItem(): SearchItem {
    return SearchItem(
        data,
        transcription ?: "[]",
        translations.map { Translation(it, true) },
        translations.joinToString()
    )
}
