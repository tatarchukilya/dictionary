package ru.nblackie.dictionary.impl.presentation.edit.recycler

import ru.nblackie.core.impl.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
internal data class EditContentItem(val hint: String, val content: String) : ListItem {

    override fun viewType(): Int = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 0
    }
}