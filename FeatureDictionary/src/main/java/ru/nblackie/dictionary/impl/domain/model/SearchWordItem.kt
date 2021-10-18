package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
data class SearchWordItem(
    val word: String,
    val transcription: String,
    val translationRemote: List<String>,
    val translationLocal: List<String>
) : ListItem {
    override fun viewType() = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 0
    }
}