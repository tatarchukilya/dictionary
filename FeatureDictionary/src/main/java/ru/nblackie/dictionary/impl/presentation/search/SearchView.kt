package ru.nblackie.dictionary.impl.presentation.search

import ru.nblackie.dictionary.impl.presentation.core.SearchInput
import ru.nblackie.dictionary.impl.presentation.core.SelectWord
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.SearchState

/**
 * @author Ilya Tatarchuk
 */
internal interface SearchView {

    // State
    fun setState(state: SearchState)

    //Action
    fun clearSearch()

    fun select(action: SelectWord)

    fun search(action: SearchInput)

    fun switchSearch(isLocal: Boolean)

    //Event
    fun showPreview()
}