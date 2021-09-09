package ru.nblackie.dictionary.impl.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nblackie.core.recycler.ListItem
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.core.viewmodel.SingleLiveEvent
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchViewModel(private val useCase: DictionaryUseCase) : ViewModel() {

    private var searchJob: Job? = null
    private val debounce = 300L

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress
    private val _words = MutableLiveData<MutableList<ListItem>>()
    val words: LiveData<MutableList<ListItem>>
        get() = _words
    private val _editWord = SingleLiveEvent<Word>()
    val editWord: LiveData<Word>
        get() = _editWord

    fun search(input: String) {
        searchJob?.cancel()
        if (input.isBlank()) {
            _words.postValue(emptyList())
            _progress.postValue(false)
            return
        }
        searchJob = viewModelScope.launch {
            _progress.postValue(true)
            delay(debounce)
            runCatching {
                useCase.search(input)
            }.onSuccess {
                _progress.postValue(false)
                _words.postValue(resultList(it))
            }.onFailure {
                _progress.postValue(false)
                _words.postValue(emptyList())
                Log.i("<>", "error", it)
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
    }

    private fun resultList(newItems: List<ListItem>): MutableList<ListItem> {
        return emptyList().apply {
            addAll(1, newItems)
        }
    }

    private fun emptyList(): MutableList<ListItem> {
        return mutableListOf<ListItem>().apply {
            add(EmptyItem(48))
        }
    }
}
