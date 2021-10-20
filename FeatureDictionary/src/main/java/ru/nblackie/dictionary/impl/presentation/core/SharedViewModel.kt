package ru.nblackie.dictionary.impl.presentation.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.impl.data.model.Translation
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

    //Selected Word
    private var previewData = PreviewData()
        set(value) {
            field = value
            _previewStateNew.value = value.toPreviewStateNew()
        }

    //Preview
    private val _previewStateNew = MutableStateFlow(PreviewStateNew())
    val previewStateNew: StateFlow<PreviewStateNew>
        get() = _previewStateNew.asStateFlow()

    //Edit
    private val _editedState = MutableStateFlow(EditState())

    val editedState: StateFlow<EditState>
        get() = _editedState.asStateFlow()

    //Search
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState.asStateFlow()

    fun setInput(input: String) {
        _searchState.value = searchState.value.setInput(input)
        search()
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
                useCase.combineSearch(input)
            }.onSuccess {
                _searchState.value = searchState.value.copy(items = searchResultList(it))
                it.forEach { res ->
                    Log.i("<>", res.toString())
                }
            }.onFailure {
                _searchState.value = searchState.value.copy(items = emptyList())
                Log.i("<>", "error", it)
            }
        }
    }

    private fun searchDb(input: String) {
        searchJob = viewModelScope.launch {
            runCatching {
                useCase.searchDb(input)
            }.onSuccess {
                _searchState.value = searchState.value.copy(items = searchResultList(it))
            }.onFailure {
                Log.i("<>", "error", it)
            }
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
    fun select(position: Int) {
        (searchState.value.items[position] as SearchItem).run {
            val newState = PreviewData(
                word, transcription, mutableListOf<Translation>().apply {
                    translation.forEach {
                        add(it)
                    }
                }
            )
            previewData = newState
        }
    }

    fun addToPersonal() {
        //TODO save to db
        val newState = previewData.copy(isAdded = !previewData.isAdded)
        previewData = newState
    }

    fun selectTranslation(position: Int) {
        val data = getTranslationByIndex(position)
        setEditState(data, position)
        //Event show editFragment
    }

    private fun addToDB(position: Int) {
        viewModelScope.launch {
            useCase.addTranslation(previewData.word, previewData.transcription, previewData.translation[position].data)
        }
    }

    fun handleAction(action: Action) {
        when (action) {
            is SelectTranslation -> selectTranslation(action.position)
            is Add -> addToDB(action.position)
        }
    }

    /**Edit**/
    fun editTranslation(translation: String) {
        setEditState(translation, editedState.value.position)
    }

    fun addTranslation() {
        setEditState("", previewData.translation.size)
    }

    private fun setEditState(translation: String, position: Int) {
        val oldData = getTranslationByIndex(position)
        val newState = EditState(translation, position, oldData != translation)
        _editedState.value = newState
    }

    fun saveChanges() {
        val newList = previewData.translation.toMutableList()
        editedState.value.run {
            if (position > previewData.translation.lastIndex) {
                newList.add(Translation(editedState.value.translation, false))
            } else {
                when (translation.isBlank()) {
                    true -> newList.removeAt(position)
                    false -> newList[position] = newList[position].copy(data = translation)
                }
            }
        }
        previewData = previewData.copy(translation = newList)
    }

    private fun getTranslationByIndex(index: Int): String {
        return try {
            previewData.translation[index].data
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

    private data class PreviewData(
        val word: String = "",
        val transcription: String = "",
        var translation: List<Translation> = listOf(),
        val isAdded: Boolean = false
    )

    data class PreviewStateNew(
        val word: String = "",
        val isAdded: Boolean = false,
        val transcriptions: List<TranscriptionItem> = listOf(),
        val translations: List<TranslationItem> = listOf()
    )

    private fun PreviewData.toPreviewStateNew(): PreviewStateNew {
        return PreviewStateNew(
            word,
            isAdded,
            listOf(TranscriptionItem(transcription)),
            mutableListOf<TranslationItem>().apply {
                translation.forEach {
                    add(TranslationItem(it.data, it.isAdded))
                }
            }
        )
    }

    data class EditState(
        val translation: String = "",
        val position: Int = -1,
        val wasChanged: Boolean = false
    )

    data class SearchState(
        val input: String = "",
        val isCache: Boolean = true,
        val items: List<TypedItem> = listOf(),
        val isClearable: Boolean = false,
        val isSwitchable: Boolean = false
    )

    private fun SearchState.clear(): SearchState {
        return copy(
            input = "",
            isClearable = false,
            isSwitchable = false
        )
    }

    private fun SearchState.setInput(input: String): SearchState {
        return this.copy(
            input = input,
            items = if (input.isNotBlank()) items else listOf(),
            isClearable = input.isNotEmpty(),
            isSwitchable = input.isNotEmpty()
        )
    }
}