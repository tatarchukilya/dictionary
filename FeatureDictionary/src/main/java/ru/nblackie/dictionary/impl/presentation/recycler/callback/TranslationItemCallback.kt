package ru.nblackie.dictionary.impl.presentation.recycler.callback

import androidx.recyclerview.widget.DiffUtil
import ru.nblackie.dictionary.impl.domain.model.TranslationItem

/**
 * @author Ilya Tatarchuk
 */
internal class TranslationItemCallback : DiffUtil.ItemCallback<TranslationItem>() {

    override fun areItemsTheSame(oldItem: TranslationItem, newItem: TranslationItem): Boolean {
        return oldItem.translation.data == newItem.translation.data
    }

    override fun areContentsTheSame(oldItem: TranslationItem, newItem: TranslationItem): Boolean {
        return oldItem.translation == newItem.translation
    }
}