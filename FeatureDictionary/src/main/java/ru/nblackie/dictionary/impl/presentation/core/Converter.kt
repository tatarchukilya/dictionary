package ru.nblackie.dictionary.impl.presentation.core

import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.PreviewState

/**
 * @author Ilya Tatarchuk
 */

internal fun SearchItem.toPreview(): PreviewState {
    return PreviewState(
        word,
        transcription?.let {
            listOf(TranscriptionItem(it))
        } ?: listOf(),
        translation.map {
            TranslationItem(it)
        })
}
