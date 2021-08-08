package ru.nblackie.dictionary.impl.presentation.dictionary

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.nblackie.dictionary.impl.domain.interactor.DictionaryInteractor

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryViewModel(private val interactor: DictionaryInteractor) : ViewModel() {

    init {
        Log.i("<>DictionaryViewModel", "init")
    }
}