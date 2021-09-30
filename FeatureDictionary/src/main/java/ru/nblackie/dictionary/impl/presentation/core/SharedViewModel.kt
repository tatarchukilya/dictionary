package ru.nblackie.dictionary.impl.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class SharedViewModel(
    private val useCase: DictionaryUseCase,
    private val resourceManager: ResourceManager
) : ViewModel() {

    private var searchJob: Job? = null

    //Edit
    private var editedTranslationData = Pair(-1, "")
        set(value) {
            field = value
            _editTranscription.postValue(field.second)
        }
    private val _editTranscription = MutableLiveData<String>()
    val editTranslation: LiveData<String>
        get() = _editTranscription

    // Preview
    private var word: Word = Word()
        set(value) {
            field = value
            _selectedItem.postValue(value.toWordData())
        }
    private val _selectedItem = MutableLiveData<WordData>()
    val selectedItem: LiveData<WordData>
        get() = _selectedItem

    //Search
    private val _progressSearch = MutableLiveData<Boolean>()
    val progressSearch: LiveData<Boolean>
        get() = _progressSearch
    private val _searchResult = MutableLiveData<MutableList<ListItem>>()
    val searchResult: LiveData<MutableList<ListItem>>
        get() = _searchResult

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

    //Preview
    fun select(position: Int) {
        (_searchResult.value?.get(position) as SearchWordItem).let {
            word = it.toWord()
        }
    }

    fun saveTranslation() {
        editedTranslationData.first.let { index ->
            word.translation[index] = editedTranslationData.second
            _selectedItem.postValue(word.toWordData())
        }
        //word.translation[position - 1] = string
    }

    //Edit
    fun setEditedTranslation(translation: String, position: Int) {
        editedTranslationData = Pair(position - 1, translation)
    }

    fun rollback() {
        editedTranslationData.first.let { index ->
            editedTranslationData = Pair(index, word.translation[index])
        }
    }

    fun setEditedTranslation(string: String) {
        editedTranslationData = Pair(editedTranslationData.first, string)
    }


    override fun onCleared() {
        searchJob?.cancel()
    }

    fun add() {
        _selectedItem.value?.let {
            val newData = it.copy(isAdded = !it.isAdded)
            _selectedItem.postValue(newData)
        }
    }

    private data class Word(
        val word: String = "",
        val transcription: String = "",
        val translation: MutableList<String> = mutableListOf(),
        val isAdded: Boolean = false
    )

    internal data class WordData(
        val word: String,
        val items: List<ListItem>,
        val isAdded: Boolean
    )

    private fun Word.toWordData(): WordData {
        return WordData(
            word,
            mutableListOf<ListItem>().apply {
                add(TranscriptionItem(transcription))
                translation.forEachIndexed { index, s ->
                    add(TranslationItem(s, index % 2 == 0))
                }
            }, isAdded
        )
    }

    private fun SearchWordItem.toWord(): Word = Word(
        word, transcription, translation.toMutableList(), isAdded
    )

    companion object {
        private const val DEBOUNCE = 300L
    }

    
}