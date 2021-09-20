package ru.nblackie.dictionary.impl.presentation.converter

import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.model.PreviewDataItem

/**
 * @author tatarchukilya@gmail.com
 */
internal fun SearchWordItem.toPreview(manager: ResourceManager): MutableList<ListItem> {

    return mutableListOf<ListItem>().apply {
        add(PreviewDataItem(manager.getString(R.string.transcription), listOf(transcription)))
        add(PreviewDataItem(manager.getString(R.string.translation), translation))
    }
}

