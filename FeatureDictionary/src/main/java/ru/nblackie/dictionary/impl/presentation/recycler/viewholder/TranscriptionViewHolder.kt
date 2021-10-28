package ru.nblackie.dictionary.impl.presentation.recycler.viewholder

import android.view.View
import android.widget.TextView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem

/**
 * @author tatarchukilya@gmail.com
 */
internal class TranscriptionViewHolder(view: View) : BindViewHolder<TranscriptionItem>(view) {

    private val transcriptionView = view.findViewById<TextView>(R.id.transcription)

    override fun onBind(item: TranscriptionItem) {
        transcriptionView.text = item.transcription
    }
}