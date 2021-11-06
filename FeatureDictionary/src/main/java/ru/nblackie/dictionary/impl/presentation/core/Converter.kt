package ru.nblackie.dictionary.impl.presentation.core

import ru.nblackie.dictionary.impl.presentation.core.DetailSource.*
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.DetailState
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.SearchItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranscriptionItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranslationItem

/**
 * @author Ilya Tatarchuk
 */

internal fun SearchItem.toDetail(): DetailState {
    return DetailState(
        word,
        transcription?.let {
            listOf(TranscriptionItem(it))
        } ?: listOf(),
        translation.map {
            TranslationItem(it)
        }, SEARCH)
}

internal fun DictionaryItem.toDetail(): DetailState {
    return DetailState(
        word,
        transcription?.let {
            listOf(TranscriptionItem(it))
        } ?: listOf(),
        translation.map {
            TranslationItem(it)
        }, DICTIONARY)
}
