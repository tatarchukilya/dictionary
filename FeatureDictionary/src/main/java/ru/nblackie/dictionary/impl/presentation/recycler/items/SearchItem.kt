package ru.nblackie.dictionary.impl.presentation.recycler.items

import ru.nblackie.dictionary.impl.data.model.Translation

/**
 * @author tatarchukilya@gmail.com
 */
internal data class SearchItem(
    val word: String,
    val transcription: String?,
    val translation: List<Translation>,
    val joinString: CharSequence,
    override val type: ItemType = ItemType.SEARCH,
) : TypedItem