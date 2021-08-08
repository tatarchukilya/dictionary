package ru.nblackie.dictionary.impl.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.nblackie.dictionary.impl.domain.interactor.DictionaryInteractor

/**
 * @author tatarchukilya@gmail.com
 */
class SearchViewModel(private val interactor: DictionaryInteractor) : ViewModel() {

    init {
        Log.i("<>SearchViewModel", "init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("<>SearchViewModel", "onCleared")
    }
}