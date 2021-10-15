package ru.nblackie.dictionary.impl.domain.converter

import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */

fun Word.toItem(): SearchWordItem = SearchWordItem(word, translation, transcription)

fun SearchResult.toItem(): SearchWordItem = SearchWordItem(word, translation, transcription ?: "")