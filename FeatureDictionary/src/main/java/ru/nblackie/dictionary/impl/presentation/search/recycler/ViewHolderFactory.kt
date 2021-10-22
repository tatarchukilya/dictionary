package ru.nblackie.dictionary.impl.presentation.search.recycler

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
internal fun viewHolderFactoryMethod(
    parent: ViewGroup,
    type: Int,
    actionObserver: (Action) -> Unit
): BindViewHolder<TypedItem> {
    @Suppress("UNCHECKED_CAST")
    return when (type) {
        ItemType.EMPTY.code -> EmptyViewHolder.create(parent)
        ItemType.SEARCH_TEXT.code -> SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_search_word, parent, false),
            actionObserver
        )
        else -> throw IllegalArgumentException("Illegal viewType $type")
    } as BindViewHolder<TypedItem>
}