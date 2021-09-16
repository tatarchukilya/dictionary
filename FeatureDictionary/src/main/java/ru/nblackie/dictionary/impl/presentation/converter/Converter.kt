package ru.nblackie.dictionary.impl.presentation.converter

import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.presentation.data.WordArgs

/**
 * @author tatarchukilya@gmail.com
 */
fun SearchWordItem.toWordArgs(): WordArgs {
    return WordArgs(word, transcription, translation)
}
