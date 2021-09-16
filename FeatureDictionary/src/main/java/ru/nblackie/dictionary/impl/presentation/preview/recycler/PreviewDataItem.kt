package ru.nblackie.dictionary.impl.presentation.preview.recycler

import ru.nblackie.core.recycler.ListItem

/**
 * @author tatarchukilya@gmail.com
 */
internal class PreviewDataItem(internal val title: String, internal val content: List<String>) :
    ListItem {

    override fun viewType(): Int = VIEW_TYPE

    companion object {
        const val VIEW_TYPE = 0
    }
}