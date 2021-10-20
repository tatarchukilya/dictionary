package ru.nblackie.dictionary.impl.presentation.search

import ru.nblackie.dictionary.impl.domain.model.TypedItem

/**
 * @author Ilya Tatarchuk
 */
internal interface SearchView {

    fun setItems(items: List<TypedItem>)

    fun progressVisibility(isVisible: Boolean)

    fun clearSearch()

    fun select(position: Int)

    fun search(input: String)

    fun switchSearch(isLocale: Boolean)

    fun hideSwitchNow()

    fun setSwitchVisibility(isVisible: Boolean)

    fun  setProgressVisibility(isVisible: Boolean)
}