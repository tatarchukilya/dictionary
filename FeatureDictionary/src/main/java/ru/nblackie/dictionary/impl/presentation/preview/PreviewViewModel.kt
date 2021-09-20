package ru.nblackie.dictionary.impl.presentation.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase
import ru.nblackie.dictionary.impl.domain.model.PreviewDataItem

/**
 * @author tatarchukilya@gmail.com
 */
internal class PreviewViewModel(private val useCase: DictionaryUseCase) : ViewModel() {

    private val _items = MutableLiveData<MutableList<ListItem>>()
    val items: LiveData<MutableList<ListItem>>
        get() = _items

    private val _isAdded = MutableLiveData(false)
    val isAdded: LiveData<Boolean>
        get() = _isAdded

    fun setData(
        translationTitle: String,
        translation: List<String>?,
        transcriptionTitle: String,
        transcription: String?
    ) {
        val list = mutableListOf<ListItem>()
        if (transcription != null && translation != null) {
            list.add(PreviewDataItem(transcriptionTitle, listOf(transcription)))
            list.add(PreviewDataItem(translationTitle, translation))
        }
        _items.postValue(list)
    }

    fun add() {
        val value = isAdded.value ?: false
        _isAdded.postValue(!value)
    }
}