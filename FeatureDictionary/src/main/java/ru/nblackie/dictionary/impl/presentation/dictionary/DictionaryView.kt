package ru.nblackie.dictionary.impl.presentation.dictionary

import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem

/**
 * @author Ilya Tatarchuk
 */
internal interface DictionaryView {

    //State
    fun setItem(items: List<TypedItem>)

    //Action
    fun selectWord(action: Action)

    fun clickSearch()

    //Event
    fun showDetail()

    fun showSearch()
}