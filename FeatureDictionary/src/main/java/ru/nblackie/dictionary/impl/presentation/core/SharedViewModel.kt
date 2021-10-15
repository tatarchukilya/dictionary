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
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase

/**
 * @author tatarchukilya@gmail.com
 */
internal class SharedViewModel(private val useCase: DictionaryUseCase) : ViewModel() {

    private var remoteSearchJob: Job? = null
    private var dbSearchJob: Job? = null

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

    fun search(input: String) {
        if (input.isBlank()) {
            clearSearch()
            return
        }
        _searchState.value =
            _searchState.value.copy(input = input, progressVisible = true, clearVisible = true, switchVisible = true)
        if (searchState.value.isLocal) searchDb(input) else searchRemote(input)
    }

    private fun searchRemote(input: String) {
        // if (input.isBlank()) {
        //     clearSearch()
        //     return
        // }
        remoteSearchJob = viewModelScope.launch {
            // _searchState.value =
            //     _searchState.value.copy(progressVisible = true, clearVisible = true, switchVisible = true)
            delay(DEBOUNCE)
            runCatching {
                useCase.searchRemote(input)
            }.onSuccess {
                _searchState.value =
                    _searchState.value.copy(remoteItems = searchResultList(it), progressVisible = false)
            }.onFailure {
                _searchState.value = _searchState.value.copy(remoteItems = emptyList(), progressVisible = false)
                Log.i("<>", "error", it)
            }
        }
    }

    private fun searchDb(input: String) {
        dbSearchJob = viewModelScope.launch {
            delay(DEBOUNCE)
            runCatching {
                useCase.searchDb(input)
            }.onSuccess {
                _searchState.value = searchState.value.copy(dbItems = searchResultList(it))
            }.onFailure {
                Log.i("<>", "error", it)
            }
        }
    }

    private fun clearSearch() {
        dbSearchJob?.cancel()
        remoteSearchJob?.cancel()
        _searchState.value = SearchState(
            isLocal = true,
            remoteItems = listOf(),
            dbItems = listOf(),
            progressVisible = false,
            clearVisible = false,
            switchVisible = false
        )
    }

    fun switchSearch(isLocal: Boolean) {
        _searchState.value = searchState.value.copy(isLocal = isLocal)
        search(searchState.value.input)
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
        (searchState.value.remoteItems[position] as SearchWordItem).run {
            val newState = PreviewData(
                word, transcription, mutableListOf<Pair<String, Boolean>>().apply {
                    translation.forEach { str ->
                        add(Pair(str, false))
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
            useCase.addTranslation(previewData.word, previewData.transcription, previewData.translation[position].first)
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
                newList.add(Pair(editedState.value.translation, false))
            } else {
                when (translation.isBlank()) {
                    true -> newList.removeAt(position)
                    false -> newList[position] = newList[position].copy(first = translation)
                }
            }
        }
        previewData = previewData.copy(translation = newList)
    }

    private fun getTranslationByIndex(index: Int): String {
        return try {
            previewData.translation[index].first
        } catch (e: Exception) {
            ""
        }
    }

    /**Edit*/

    override fun onCleared() {
        remoteSearchJob?.cancel()
    }

    companion object {
        private const val DEBOUNCE = 300L
    }

    private data class PreviewData(
        val word: String = "",
        val transcription: String = "",
        var translation: List<Pair<String, Boolean>> = listOf(),
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
                    add(TranslationItem(it.first, it.second))
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
        val isLocal: Boolean = true,
        val remoteItems: List<ListItem> = listOf(),
        val dbItems: List<ListItem> = listOf(),
        val progressVisible: Boolean = false,
        val clearVisible: Boolean = false,
        val switchVisible: Boolean = false
    )
}