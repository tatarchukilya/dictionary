package ru.nblackie.dictionary.impl.domain.converter

import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */

fun SearchResult.toItem(): SearchWordItem {
    val remote = remoteTranslation.toMutableList()
    localTranslation.forEach {
        remote.remove(it)
    }
    return SearchWordItem(word, transcription ?: "", remote, localTranslation)
}
