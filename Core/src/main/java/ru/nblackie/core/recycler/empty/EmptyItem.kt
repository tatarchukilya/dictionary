package ru.nblackie.core.recycler.empty

import ru.nblackie.core.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class EmptyItem(val heightDp: Int) : ListItem {

    override fun viewType(): Int = -1
}