package ru.nblackie.dictionary.impl.presentation.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.EmptyItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem

/**
 * @author Ilya Tatarchuk
 */
internal class DictionaryItemCallback : DiffUtil.ItemCallback<TypedItem>() {

    override fun areItemsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
        if (oldItem.type.code == newItem.type.code) {
            if (oldItem is EmptyItem) {
                return true
            }
            if (oldItem is DictionaryItem) {
                return oldItem.word == (newItem as DictionaryItem).word
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
        return when (oldItem) {
            is EmptyItem -> true
            is DictionaryItem -> (newItem as DictionaryItem).let {
                oldItem.translation == it.translation
            }
            else -> false
        }
    }
}