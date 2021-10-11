package ru.nblackie.dictionary.impl.presentation.search

import ru.nblackie.core.impl.recycler.ListItem

/**
 * @author Ilya Tatarchuk
 */
interface SearchView {

    fun setItems(items: List<ListItem>)

    fun progressVisibility(isVisible: Boolean)

    fun clearSearch()

    fun select(position: Int)

    fun search(input: String)

    fun selectLocal()

    fun selectRemote()

    fun hideSwitchNow()

    fun setSwitchVisibility(isVisible: Boolean)

    fun  setProgressVisibility(isVisible: Boolean)
}