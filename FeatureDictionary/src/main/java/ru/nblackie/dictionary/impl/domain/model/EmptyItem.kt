package ru.nblackie.dictionary.impl.domain.model

/**
 * @author tatarchukilya@gmail.com
 */
internal data class EmptyItem(val heightDp: Int, override val type: ItemType = ItemType.EMPTY) : TypedItem