package ru.nblackie.dictionary.impl.presentation.dictionary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryViewModelNew(
    private val useCase: DictionaryUseCase,
    private val handle: SavedStateHandle
) : ViewModel() {
}