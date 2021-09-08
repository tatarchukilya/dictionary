package ru.nblackie.dictionary.impl.presentation.dictionary

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.nblackie.dictionary.impl.domain.interactor.DictionaryUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryViewModel(private val useCase: DictionaryUseCase) : ViewModel() {

    init {
        Log.i("<>DictionaryViewModel", "init")
    }
}