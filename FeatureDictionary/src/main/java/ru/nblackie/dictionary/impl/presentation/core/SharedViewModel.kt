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
import ru.nblackie.dictionary.impl.domain.usecase.UseCase
import ru.nblackie.dictionary.impl.domain.usecase.create.AddTranslationsUseCase
import ru.nblackie.dictionary.impl.domain.usecase.delete.DeleteTranslationUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.DbSearchUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.DictionaryUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteCountUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteSearchUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class SharedViewModel(
    private val useCases: Map<Class<out UseCase>, UseCase>
) : ViewModel() {

    private var countJob: Job? = null
    private var dictionaryJob: Job? = null
    private var searchJob: Job? = null

    //Dictionary
    private val _dictionaryState = MutableStateFlow<List<TypedItem>>(listOf())
    val dictionaryState: StateFlow<List<TypedItem>> = _dictionaryState.asStateFlow()

    //Search
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchEvent: Channel<Event> = Channel()
    val searchEvent = _searchEvent.receiveAsFlow()

    //Preview
    private val _previewState = MutableStateFlow(PreviewState())
    val previewState: StateFlow<PreviewState> = _previewState.asStateFlow()

    private val _previewEvent = Channel<Event>()
    val previewEvent = _previewEvent.receiveAsFlow()

    //Add
    private val _newTranslationState = MutableStateFlow(AddTranslationState())
    val newTranslationState: StateFlow<AddTranslationState> = _newTranslationState.asStateFlow()

    private val _newTranslationEvent = Channel<Event>()
    val newTranslationEvent = _newTranslationEvent.receiveAsFlow()

    init {
        getDictionary()
        getCount()
    }

    fun handleAction(action: Action) {
        when (action) {
            is SwitchSearch -> switchSearch(action.isLocal)
            is ClearSearch -> setSearchInput("")
            is SearchInput -> setSearchInput(action.input)
            is MatchTranslation -> matchTranslation(action.position)
            is SelectWord -> selectWord(action.position)
            is AddTranslation -> showNewTranslationView()
            is NewTranslation -> setNewTranslationState(action.input)
            is SaveNewTranslation -> saveNewTranslation()
        }
    }

    override fun onCleared() {
        countJob?.cancel()
        dictionaryJob?.cancel()
        searchJob?.cancel()
    }

    //Dictionary
    private fun getDictionary() {
        dictionaryJob = viewModelScope.launch {
            runCatching {
               getUseCase(DictionaryUseCase::class.java).run()
            }.onSuccess {
                _dictionaryState.value = listResultList(it)
            }.onFailure {

            }
        }
    }

    // //Search
    private fun setSearchInput(input: String) {
        if (input != searchState.value.input) {
            _searchState.value = searchState.value.setSearchInput(input)
            if (input.isBlank()) searchJob?.cancel() else search()
        }
    }

    private fun switchSearch(isLocal: Boolean) {
        _searchState.value = searchState.value.copy(isCache = isLocal)
        search()
    }

    private fun search() {
        searchJob?.cancel()
        searchState.value.run {
            if (input.isNotBlank()) {
                if (isCache) searchDb(input) else searchRemote(input)
            }
        }
    }

    private fun selectWord(position: Int) {
        _previewState.value = (searchState.value.items[position] as SearchItem).toPreview()
        viewModelScope.launch { _searchEvent.send(ShowPreview) }
    }

    private fun searchRemote(input: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            runCatching {
                _searchState.value = searchState.value.copy(inProgress = true)
                getUseCase(RemoteSearchUseCase::class.java).run(input)
            }.onSuccess {
                setSearchItems(it)
            }.onFailure {
                setSearchItems(listOf())
                Log.i("SharedViewModel", "remote search failed", it)
            }
        }
    }

    private fun searchDb(input: String) {
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            runCatching {
                getUseCase(DbSearchUseCase::class.java).run(input)
                //dbSearch.run(input)
            }.onSuccess {
                setSearchItems(it)
            }.onFailure {
                Log.d("SharedViewModel", "db search failed", it)
            }
        }
    }

    private fun getCount() {
        countJob = viewModelScope.launch {
            runCatching {
                getUseCase(RemoteCountUseCase::class.java).run()
            }.onSuccess {
                _searchState.value = searchState.value.copy(count = it)
            }.onFailure {
                Log.d("SharedViewModel", "loading count failed", it)
            }
        }
    }

    private fun setSearchItems(items: List<SearchItem>) {
        _searchState.value = searchState.value.copy(items = listResultList(items), inProgress = false)
        items.find { it.word == previewState.value.word }?.let {
            _previewState.value = it.toPreview()
        } ?: run {
            _previewState.value = previewState.value.copy(translations = listOf())
        }
    }

    private fun listResultList(newItems: List<TypedItem>): MutableList<TypedItem> {
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
    private fun matchTranslation(position: Int) {
        viewModelScope.launch {
            previewState.value.translations[position].translation.let {
                if (it.isAdded) {
                    getUseCase(DeleteTranslationUseCase::class.java).run(previewState.value.word, it.data)
                } else {
                    addTranslation(previewState.value.word, previewState.value.transcriptions[0].transcription, it.data)
                }
            }
            search()
        }
    }

    private suspend fun addTranslation(word: String, transcription: String, translation: String) {
        getUseCase(AddTranslationsUseCase::class.java).run(word, transcription, translation)
    }

    private fun showNewTranslationView() {
        _newTranslationState.value = AddTranslationState()
        viewModelScope.launch {
            _previewEvent.send(ShowNewWordView)
        }
    }

    //Add
    private fun setNewTranslationState(input: String) {
        _newTranslationState.value = translationSateByInput(input)
    }

    private fun saveNewTranslation() {
        viewModelScope.launch {
            previewState.value.let {
                addTranslation(it.word, it.transcriptions[0].transcription, newTranslationState.value.translation)
            }
            _newTranslationEvent.send(StopSelf)
            search()
        }
    }

    //Search
    data class SearchState(
        val count: Int = 0,
        val inProgress: Boolean = false,
        val input: String = "",
        val isCache: Boolean = true,
        val items: List<TypedItem> = listOf(),
        val isClearable: Boolean = false,
        val isSwitchable: Boolean = false
    )

    private fun SearchState.setSearchInput(input: String): SearchState {
        return this.copy(
            input = input,
            items = if (input.isNotBlank()) items else listOf(),
            isClearable = input.isNotEmpty(),
            isSwitchable = input.isNotEmpty()
        )
    }

    //Preview
    data class PreviewState(
        val word: String = "",
        val transcriptions: List<TranscriptionItem> = listOf(),
        val translations: List<TranslationItem> = listOf()
    )

    //Add
    data class AddTranslationState(
        val translation: String = "",
        val wasChanged: Boolean = false
    )

    @Suppress("UNCHECKED_CAST")
    private fun <T : UseCase> getUseCase(clazz: Class<T>): T {
        return useCases[clazz] as T
    }

    private fun translationSateByInput(input: String) = AddTranslationState(input, input.isNotBlank())

    companion object {
        private const val DEBOUNCE = 300L
    }
}