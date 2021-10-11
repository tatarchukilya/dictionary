package ru.nblackie.dictionary.impl.presentation.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private var searchJob: Job? = null

    //Selected Word
    private var previewData = PreviewData()
        set(value) {
            field = value
            _previewState.value = value.toPreviewState()
        }

    //Preview
    private val _previewState = MutableStateFlow(PreviewState())
    val previewState: StateFlow<PreviewState>
        get() = _previewState.asStateFlow()

    //Edit
    private val _editedState = MutableStateFlow(EditState())

    val editedState: StateFlow<EditState>
        get() = _editedState.asStateFlow()

    //Search
    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState.asStateFlow()

    fun search(input: String) {
        searchJob?.cancel()
        if (input.isBlank()) {
            _searchState.value = SearchState(listOf(),
                progressVisible = false,
                clearVisible = false,
                switchVisible = false
            )
            return
        }
        searchJob = viewModelScope.launch {
            _searchState.value =
                _searchState.value.copy(progressVisible = true, clearVisible = true, switchVisible = true)
            delay(DEBOUNCE)
            runCatching {
                useCase.search(input)
            }.onSuccess {
                _searchState.value = _searchState.value.copy(items = searchResultList(it), progressVisible = false)
            }.onFailure {
                _searchState.value = _searchState.value.copy(items = emptyList(), progressVisible = false)
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
        (searchState.value.items[position] as SearchWordItem).run {
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
        val index = position - 1
        val data = getTranslationByIndex(index)
        setEditState(data, index)
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
        val newState = EditState("", translation, position, oldData != translation)
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
        searchJob?.cancel()
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

    data class PreviewState(
        val word: String = "",
        val isAdded: Boolean = false,
        val items: List<ListItem> = listOf()
    )

    data class EditState(
        val title: String = "",
        val translation: String = "",
        val position: Int = -1,
        val wasChanged: Boolean = false
    )

    data class SearchState(
        val items: List<ListItem> = listOf(),
        val progressVisible: Boolean = false,
        val clearVisible: Boolean = false,
        val switchVisible: Boolean = false
    )

    private fun PreviewData.toPreviewState(): PreviewState {
        return PreviewState(
            word, isAdded, mutableListOf<ListItem>().apply {
                add(TranscriptionItem(transcription))
                translation.forEach {
                    add(TranslationItem(it.first, it.second))
                }
            }
        )
    }
}