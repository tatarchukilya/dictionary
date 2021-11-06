package ru.nblackie.dictionary.impl.presentation.recycler.items

/**
 * @author tatarchukilya@gmail.com
 */
internal data class EmptyItem(val heightDp: Int, override val type: ItemType = ItemType.EMPTY) : TypedItem