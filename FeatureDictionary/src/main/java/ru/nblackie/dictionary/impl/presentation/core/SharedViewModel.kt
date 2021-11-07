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
import ru.nblackie.dictionary.impl.domain.usecase.UseCase
import ru.nblackie.dictionary.impl.domain.usecase.create.AddTranslationsUseCase
import ru.nblackie.dictionary.impl.domain.usecase.delete.DeleteTranslationUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.DbSearchUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.DictionaryUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteCountUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteSearchUseCase
import ru.nblackie.dictionary.impl.presentation.core.DetailSource.*
import ru.nblackie.dictionary.impl.presentation.core.Event.ShowAddView
import ru.nblackie.dictionary.impl.presentation.core.Event.ShowDetail
import ru.nblackie.dictionary.impl.presentation.core.Event.StopSelf
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.EmptyItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.SearchItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranscriptionItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranslationItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem
import java.lang.IllegalStateException

/**
 * @author tatarchukilya@gmail.com
 */
internal class SharedViewModel(
    private val useCases: Map<Class<out UseCase>, UseCase>,
) : ViewModel() {

    private var countJob: Job? = null
    private var startJob: Job? = null
    private var searchJob: Job? = null

    //Dictionary
    private val _dictionaryState = MutableStateFlow<List<TypedItem>>(listOf())
    val dictionaryState: StateFlow<List<TypedItem>> = _dictionaryState.asStateFlow()

    private val _dictionaryEvent: Channel<Event> = Channel()
    val dictionaryEvent = _dictionaryEvent.receiveAsFlow()

    //Search
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    private val _searchEvent: Channel<Event> = Channel()
    val searchEvent = _searchEvent.receiveAsFlow()

    //Detail
    private val _detailState = MutableStateFlow(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    private val _detailEvent = Channel<Event>()
    val detailEvent = _detailEvent.receiveAsFlow()

    //Add
    private val _newTranslationState = MutableStateFlow(AddTranslationState())
    val newTranslationState: StateFlow<AddTranslationState> = _newTranslationState.asStateFlow()

    private val _newTranslationEvent = Channel<Event>()
    val newTranslationEvent = _newTranslationEvent.receiveAsFlow()

    init {
        startJob = viewModelScope.launch {
            getDictionary()
            remoteCount()
        }
    }

    fun handleAction(action: Action) = when (action) {
        is Action.DictionarySelect -> selectDictionary(action.position)
        is Action.SwitchSearch -> switchSearch(action.isLocal)
        is Action.ClearSearch -> setSearchWord("")
        is Action.SearchInput -> setSearchWord(action.input)
        is Action.MatchTranslation -> matchTranslation(action.position)
        is Action.SearchSelect -> selectSearch(action.position)
        is Action.AddTranslation -> showNewTranslationView()
        is Action.NewTranslation -> setNewTranslationState(action.input)
        is Action.SaveNewTranslation -> saveNewTranslation()
        is Action.ShowSearch -> showSearch()
        is Action.SelectWord -> selectWord(action.position, action.source)
    }

    /**
     * Выбор слова для редактирования данных
     *
     * @param position позиция в списке
     * @param source для определения списка, из которого произведен выбор
     */
    private fun selectWord(position: Int, source: DetailSource) {
        when (source) {
            DICTIONARY -> {
                _detailState.value = (dictionaryState.value[position] as DictionaryItem).toDetail()
                viewModelScope.launch { _dictionaryEvent.send(ShowDetail) }
            }
            SEARCH -> {
                _detailState.value = (searchState.value.items[position] as SearchItem).toDetail()
                viewModelScope.launch { _searchEvent.send(ShowDetail) }
            }
        }
    }

    override fun onCleared() {
        countJob?.cancel()
        startJob?.cancel()
        searchJob?.cancel()
    }

    //Dictionary
    private suspend fun getDictionary() {
        runCatching {
            getUseCase(DictionaryUseCase::class.java).run()
        }.onSuccess {
            //TODO добавить item с подсказками для пустого списка
            _dictionaryState.value = listResultList(it, 64)
        }.onFailure {
            Log.i("SharedViewModel", "db dictionary failed", it)
        }
    }

    private fun selectDictionary(position: Int) {
        _detailState.value = (dictionaryState.value[position] as DictionaryItem).toDetail()
        viewModelScope.launch { _dictionaryEvent.send(ShowDetail) }
    }

    private fun showSearch() {
        setSearchWord("")
        viewModelScope.launch { _dictionaryEvent.send(Event.ShowSearch) }
    }

    //Search
    private fun setSearchWord(input: String) {
        if (input != searchState.value.input) {
            _searchState.value = searchState.value.setSearchWord(input)
            asyncSearch()
        }
    }

    private fun switchSearch(isLocal: Boolean) {
        if (isLocal != searchState.value.isCache) {
            _searchState.value = searchState.value.copy(isCache = isLocal)
            asyncSearch()
        }
    }

    /**
     * Обновляет данные. Вызывается после добавления или удпления варианта перевода.
     */
    private suspend fun update() {
        when (detailState.value.source) {
            DICTIONARY -> {
                getDictionary()
                dictionaryState.value
                    .find { item -> (item is DictionaryItem && item.word == detailState.value.word) }
                    ?.let { _detailState.value = (it as DictionaryItem).toDetail() }
                //TODO stop DetailFragment
                    ?: run { _detailState.value = detailState.value.copy(translations = listOf()) }
            }
            SEARCH -> {
                getDictionary()
                search()
                searchState.value.items
                    .find { item -> item is SearchItem && item.word == detailState.value.word }
                    ?.let { _detailState.value = (it as SearchItem).toDetail() }
                    ?: run { _detailState.value = detailState.value.copy(translations = listOf()) }
            }
            else -> throw IllegalStateException("illegal detail state source ${detailState.value.source}")
        }
    }

    private fun selectSearch(position: Int) {
        _detailState.value = (searchState.value.items[position] as SearchItem).toDetail()
        viewModelScope.launch { _searchEvent.send(ShowDetail) }
    }

    private fun asyncSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            search()
        }
    }

    private suspend fun search() {
        searchState.value.let { state ->
            if (state.input.isBlank()) {
                return
            }
            when (state.isCache) {
                true -> dbSearch(state.input)
                false -> remoteSearch(state.input)
            }
        }
    }

    private suspend fun remoteSearch(input: String) {
        delay(DEBOUNCE)
        runCatching {
            _searchState.value = searchState.value.copy(inProgress = true)
            getUseCase(RemoteSearchUseCase::class.java).run(input)
        }.onSuccess {
            setSearchItems(it)
        }.onFailure {
            setSearchItems(listOf())
            Log.d("SharedViewModel", "remote search failed", it)
        }
    }

    private suspend fun dbSearch(input: String) {
        runCatching {
            getUseCase(DbSearchUseCase::class.java).run(input)
        }.onSuccess {
            setSearchItems(it)
        }.onFailure {
            Log.d("SharedViewModel", "db search failed", it)
        }
    }

    private suspend fun remoteCount() {
        runCatching {
            getUseCase(RemoteCountUseCase::class.java).run()
        }.onSuccess {
            _searchState.value = searchState.value.copy(count = it)
        }.onFailure {
            Log.d("SharedViewModel", "loading count failed", it)
        }
    }

    private fun setSearchItems(items: List<SearchItem>) {
        _searchState.value = searchState.value.copy(items = listResultList(items, 56), inProgress = false)
        items.find { it.word == detailState.value.word }
            ?.let { _detailState.value = it.toDetail() }
            ?: run { _detailState.value = detailState.value.copy(translations = listOf()) }
    }

    private fun listResultList(newItems: List<TypedItem>, padding: Int): MutableList<TypedItem> {
        return emptyList(padding).apply {
            addAll(1, newItems)
        }
    }

    private fun emptyList(height: Int): MutableList<TypedItem> {
        return mutableListOf<TypedItem>().apply {
            add(EmptyItem(height))
        }
    }

    //Detail
    private fun matchTranslation(position: Int) {
        viewModelScope.launch {
            detailState.value.translations[position].translation.let {
                if (it.isAdded) {
                    getUseCase(DeleteTranslationUseCase::class.java).run(detailState.value.word, it.data)
                } else {
                    addTranslation(detailState.value.word, detailState.value.transcriptions[0].transcription, it.data)
                }
            }
            update()
        }
    }

    private suspend fun addTranslation(word: String, transcription: String, translation: String) {
        getUseCase(AddTranslationsUseCase::class.java).run(word, transcription, translation)
    }

    private fun showNewTranslationView() {
        _newTranslationState.value = AddTranslationState()
        viewModelScope.launch {
            _detailEvent.send(ShowAddView)
        }
    }

    //Add
    private fun setNewTranslationState(input: String) {
        _newTranslationState.value = translationSateByInput(input)
    }

    private fun saveNewTranslation() {
        viewModelScope.launch {
            detailState.value.let {
                addTranslation(it.word, it.transcriptions[0].transcription, newTranslationState.value.translation)
            }
            update()
            _newTranslationEvent.send(StopSelf)
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
        val isSwitchable: Boolean = false,
    )

    private fun SearchState.setSearchWord(input: String): SearchState {
        return this.copy(
            input = input,
            items = if (input.isNotBlank()) items else listOf(),
            isClearable = input.isNotEmpty(),
            isSwitchable = input.isNotEmpty()
        )
    }

    //Detail
    data class DetailState(
        val word: String = "",
        val transcriptions: List<TranscriptionItem> = listOf(),
        val translations: List<TranslationItem> = listOf(),
        val source: DetailSource? = null,
    )

    //Add
    data class AddTranslationState(
        val translation: String = "",
        val wasChanged: Boolean = false,
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