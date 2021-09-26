package ru.nblackie.dictionary.impl.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase
import ru.nblackie.dictionary.impl.presentation.converter.toPreview

/**
 * @author tatarchukilya@gmail.com
 */
class SharedViewModel(
    private val useCase: DictionaryUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    init {
        Log.i("<>", "Init DictionarySharedViewModel")
    }

    private var searchJob: Job? = null

    //Search
    private val _progressSearch = MutableLiveData<Boolean>()
    val progressSearch: LiveData<Boolean>
        get() = _progressSearch
    private val _searchResult = MutableLiveData<MutableList<ListItem>>()
    val searchResult: LiveData<MutableList<ListItem>>
        get() = _searchResult

    //Selected
    private val _selectedWord = MutableLiveData<String>()
    val selectedWord: LiveData<String>
        get() = _selectedWord
    private val _selectedWordData = MutableLiveData<MutableList<ListItem>>()
    val selectedWordData: LiveData<MutableList<ListItem>>
        get() = _selectedWordData
    private val _isAdded = MutableLiveData(false)
    val isAdded: LiveData<Boolean>
        get() = _isAdded


    fun search(input: String) {
        searchJob?.cancel()
        if (input.isBlank()) {
            _searchResult.postValue(emptyList())
            _progressSearch.postValue(false)
            return
        }
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            _progressSearch.postValue(true)
            runCatching {
                useCase.search(input)
            }.onSuccess {
                _progressSearch.postValue(false)
                _searchResult.postValue(searchResultList(it))
            }.onFailure {
                _progressSearch.postValue(false)
                _searchResult.postValue(emptyList())
                Log.i("<>", "error", it)
            }
        }
    }

    fun select(position: Int) {
        with(_searchResult.value?.get(position) as SearchWordItem) {
            _selectedWord.postValue(word)
            _selectedWordData.postValue(toPreview(resourceManager))
        }
    }

    fun clearSelected() {
        _selectedWord.postValue("")
        _selectedWordData.postValue(mutableListOf())
    }

    fun add() {
        val value = isAdded.value ?: false
        _isAdded.postValue(!value)
    }

    private fun searchResultList(newItems: List<ListItem>): MutableList<ListItem> {
        return emptyList().apply {
            addAll(1, newItems)
        }
    }

    private fun emptyList(): MutableList<ListItem> {
        return mutableListOf<ListItem>().apply {
            add(EmptyItem(48))
        }
    }

    fun logAttach(fragment: String) {
        Log.i("<>attachFragment", fragment)
    }

    override fun onCleared() {
        Log.i("<>", "onCleared DictionarySharedViewModel")
        searchJob?.cancel()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }
}