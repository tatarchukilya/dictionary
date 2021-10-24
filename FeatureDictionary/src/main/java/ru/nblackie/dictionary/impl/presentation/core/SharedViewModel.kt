package ru.nblackie.dictionary.impl.presentation.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.domain.model.TypedItem
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class SharedViewModel(private val useCase: DictionaryUseCase) : ViewModel() {

    private var searchJob: Job? = null

    //Preview
    private val _previewState = MutableStateFlow(PreviewState())
    val previewState: StateFlow<PreviewState> = _previewState.asStateFlow()

    //Edit
    private val _editedState = MutableStateFlow(EditState())
    val editedState: StateFlow<EditState>
        get() = _editedState.asStateFlow()

    //Search
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchEvent: Channel<Event> = Channel()
    val searchEvent = _searchEvent.receiveAsFlow()

    fun handleAction(action: Action) {
        when (action) {
            is SelectTranslation -> selectTranslation(action.position)
            is MatchTranslation -> {
                matchTranslation(action.position)
            }
            is SelectWord -> {
                selectWord(action.position)
                viewModelScope.launch { _searchEvent.send(ShowPreview) }
            }
        }
    }

    fun setInput(input: String) {
        if (input != searchState.value.input) {
            _searchState.value = searchState.value.setInput(input)
            if (input.isNotBlank())
                search()
        }
    }

    fun switchSearch(isCache: Boolean) {
        _searchState.value = searchState.value.copy(isCache = isCache)
        search()
    }

    fun search() {
        searchJob?.cancel()
        searchState.value.run {
            if (input.isNotBlank()) {
                if (isCache) searchDb(input) else searchRemote(input)
            }
        }
    }

    private fun searchRemote(input: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            runCatching {
                _searchState.value = searchState.value.copy(inProgress = true)
                useCase.combineSearch(input)
            }.onSuccess {
                setSearchItems(it)
            }.onFailure {
                setSearchItems(listOf())
                Log.i("<>", "error", it)
            }
        }
    }

    private fun searchDb(input: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            runCatching {
                useCase.searchDb(input)
            }.onSuccess {
                setSearchItems(it)
            }.onFailure {
                Log.i("<>", "error", it)
            }
        }
    }

    private fun setSearchItems(items: List<SearchItem>) {
        _searchState.value = searchState.value.copy(items = searchResultList(items), inProgress = false)
        items.find { it.word == previewState.value.word }?.let {
            _previewState.value = it.toPreview()
        } ?: run {
            _previewState.value = previewState.value.copy(translations = listOf())
        }
    }

    private fun searchResultList(newItems: List<TypedItem>): MutableList<TypedItem> {
        return emptyList().apply {
            addAll(1, newItems)
        }
    }

    private fun emptyList(): MutableList<TypedItem> {
        return mutableListOf<TypedItem>().apply {
            add(EmptyItem(56))
        }
    }

    //Preview
    fun selectWord(position: Int) {
        _previewState.value = (searchState.value.items[position] as SearchItem).toPreview()
    }

    fun selectTranslation(position: Int) {
        val data = getTranslationByIndex(position)
        setEditState(data, position)
        //Event show editFragment
    }

    /**
     *
     */
    private fun matchTranslation(position: Int) {
        viewModelScope.launch {
            previewState.value.translations[position].translation.let {
                if (it.isAdded) {
                    useCase.deleteTranslation(previewState.value.word, it.data)
                } else {
                    useCase.addTranslation(
                        previewState.value.word,
                        previewState.value.transcriptions[0].transcription,
                        it.data
                    )
                }
            }
            search()
        }
    }

    fun addNewTranslation(translation: String) {
        viewModelScope.launch {
            previewState.value.let {
                useCase.addTranslation(
                    it.word,
                    it.transcriptions[0].transcription,
                    translation
                )
            }
        }
    }

    private fun setEditState(translation: String, position: Int) {
        val oldData = getTranslationByIndex(position)
        val newState = EditState(translation, position, oldData != translation)
        _editedState.value = newState
    }

    private fun getTranslationByIndex(index: Int): String {
        return try {
            previewState.value.translations[index].translation.data
        } catch (e: Exception) {
            ""
        }
    }

    /**Edit*/

    override fun onCleared() {
        searchJob?.cancel()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }

    data class PreviewState(
        val word: String = "",
        val transcriptions: List<TranscriptionItem> = listOf(),
        val translations: List<TranslationItem> = listOf()
    )

    data class EditState(
        val translation: String = "",
        val position: Int = -1,
        val wasChanged: Boolean = false
    )

    data class SearchState(
        val inProgress: Boolean = false,
        val input: String = "",
        val isCache: Boolean = true,
        val items: List<TypedItem> = listOf(),
        val isClearable: Boolean = false,
        val isSwitchable: Boolean = false
    )

    private fun SearchState.setInput(input: String): SearchState {
        return this.copy(
            input = input,
            items = if (input.isNotBlank()) items else listOf(),
            isClearable = input.isNotEmpty(),
            isSwitchable = input.isNotEmpty()
        )
    }
}