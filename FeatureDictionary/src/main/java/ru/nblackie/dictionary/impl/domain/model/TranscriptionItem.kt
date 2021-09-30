package ru.nblackie.dictionary.impl.domain.model

import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
data class TranscriptionItem(val transcription: String) : ListItem {

    override fun viewType(): Int = R.layout.view_transcription_title
}