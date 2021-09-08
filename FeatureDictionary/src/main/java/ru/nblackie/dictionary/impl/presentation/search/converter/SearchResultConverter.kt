package ru.nblackie.dictionary.impl.presentation.dictionary.search.converter

import ru.nblackie.core.recycler.ListItem
import ru.nblackie.dictionary.impl.presentation.search.recycler.SingleWordItem
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
object SearchResultConverter {

    inline fun convert(words: List<Word>, crossinline click: (Word) -> Unit): MutableList<ListItem> {
        return mutableListOf<ListItem>().apply {
            words.forEachIndexed { index, word ->
                add(
                    SingleWordItem(
                        word.word,
                        word.translation.joinToString(),
                        word.transcription
                    ) { click(words[index]) })
            }
        }
    }
}