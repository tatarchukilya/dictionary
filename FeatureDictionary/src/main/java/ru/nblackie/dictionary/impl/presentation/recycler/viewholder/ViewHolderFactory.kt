package ru.nblackie.dictionary.impl.presentation.recycler.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.ItemType
import ru.nblackie.dictionary.impl.domain.model.TypedItem
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder

/**
 * @author tatarchukilya@gmail.com
 */
@Suppress("UNCHECKED_CAST")
internal fun viewHolderFactoryMethod(
    parent: ViewGroup,
    type: Int,
    actionObserver: (Action) -> Unit
): BindViewHolder<TypedItem> {
    return when (type) {
        ItemType.EMPTY.code -> EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_empty, parent, false)
        )
        ItemType.SEARCH_TEXT.code -> WordViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_search_word, parent, false),
            actionObserver
        )
        else -> throw IllegalArgumentException("Illegal viewType $type")
    } as BindViewHolder<TypedItem>
}