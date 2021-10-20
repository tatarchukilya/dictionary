package ru.nblackie.dictionary.impl.presentation.search.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.model.SearchSpannableItem
import ru.nblackie.dictionary.impl.domain.model.TypedItem

/**
 * @author Ilya Tatarchuk
 */
internal class SearchItemCallback : DiffUtil.ItemCallback<TypedItem>() {

    override fun areItemsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
        when (oldItem) {
            is SearchItem -> {
                if (newItem is SearchItem) {
                    return oldItem.word == newItem.word
                }
                if (newItem is SearchSpannableItem) {
                    return oldItem.word == newItem.word
                }
            }
            is SearchSpannableItem -> {
                if (newItem is SearchItem) {
                    return oldItem.word == newItem.word
                }
                if (newItem is SearchSpannableItem) {
                    return oldItem.word == newItem.word
                }
            }
            else -> return true
        }
        return false
    }

    override fun areContentsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
        if (oldItem.type.code == newItem.type.code) {
            return when (oldItem) {
                is EmptyItem -> true
                is SearchItem -> (newItem as SearchItem).let {
                    oldItem.translation == it.translation && oldItem.joinString == it.joinString
                }
                is SearchSpannableItem -> (newItem as SearchSpannableItem).let {
                    oldItem.translation == it.translation && oldItem.spannable.toString() == it.spannable.toString()
                }
                else -> false
            }
        }
        return false
    }
}