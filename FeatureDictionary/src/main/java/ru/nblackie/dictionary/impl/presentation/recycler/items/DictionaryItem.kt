package ru.nblackie.dictionary.impl.presentation.recycler.items

import ru.nblackie.dictionary.impl.data.model.Translation

/**
 * @author Ilya Tatarchuk
 */
internal data class DictionaryItem(
    val word: String,
    val transcription: String?,
    val translation: List<Translation>,
    val joinString: CharSequence,
    override val type: ItemType = ItemType.DICTIONARY,
) : TypedItem
