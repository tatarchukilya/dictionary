package ru.nblackie.dictionary.impl.presentation.search.recycler

import ru.nblackie.core.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class SingleWordItem(
    val word: String,
    val translation: String,
    val transcription: String,
    val click: () -> Unit
) : ListItem {
    override fun viewType() = 0
}