package ru.nblackie.dictionary.impl.presentation.search

import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel

/**
 * @author Ilya Tatarchuk
 */
internal fun SearchItem.toPreview(): SharedViewModel.PreviewState {
    return SharedViewModel.PreviewState(
        word,
        listOf(TranscriptionItem(transcription)),
        mutableListOf<TranslationItem>().apply {
            translation.forEach {
                add(TranslationItem(it.data, it.isAdded))
            }
        })
}
