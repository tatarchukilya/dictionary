package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R
import java.lang.IllegalArgumentException

/**
 * @author tatarchukilya@gmail.com
 */
internal fun viewHolderFactoryMethod(parent: ViewGroup, type: Int): BindViewHolder<out ListItem> {
    return when (type) {
        -1 -> EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_empty, parent, false)
        )
//        0 -> SingleWordViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.view_word, parent, false)
//        )
        else -> throw IllegalArgumentException("Illegal viewType $type")
    }
}