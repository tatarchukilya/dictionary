package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.dictionary.impl.data.model.Translation

/**
 * @author tatarchukilya@gmail.com
 */
internal data class SearchItem(
    val word: String,
    val transcription: String,
    val translation: List<Translation>,
    val joinString: String,
    override val type: ItemType = ItemType.SEARCH_TEXT,
) : TypedItem