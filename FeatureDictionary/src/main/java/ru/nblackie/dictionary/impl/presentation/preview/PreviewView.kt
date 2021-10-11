package ru.nblackie.dictionary.impl.presentation.preview

import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.impl.presentation.actions.Event

/**
 * @author Ilya Tatarchuk
 */
internal interface PreviewView {

    fun setItems(items: List<ListItem>)

    fun selectTranslation(event: Event)

    fun addToPersonal()
}