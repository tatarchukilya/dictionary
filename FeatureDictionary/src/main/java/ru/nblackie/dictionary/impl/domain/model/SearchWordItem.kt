package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class SearchWordItem(
    val word: String,
    val translation: String,
    val transcription: String
) : ListItem {
    override fun viewType() = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 0
    }
}