package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class SearchWordItem(
    val id: Int,
    val word: String,
    val translation: List<String>,
    val transcription: String,
    var isAdded: Boolean
) : ListItem {
    override fun viewType() = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 0
    }
}