package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class EmptyItem(val heightDp: Int) : ListItem {

    override fun viewType(): Int = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = -1
    }
}