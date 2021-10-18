package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */
class SingleWordViewHolder(
    view: View,
    private val actionClick: (view: View?, position: Int) -> Unit
) :
    BindViewHolder<SearchWordItem>(view), View.OnClickListener {

    private val wordView = view.findViewById<TextView>(R.id.word_text_view)
    private val remoteTranslationView = view.findViewById<TextView>(R.id.remote_translation)
    private val localTranslationView = view.findViewById<TextView>(R.id.local_translation)

    init {
        wordView.transitionName = itemView.context.getString(R.string.search_preview_transition)
        itemView.setOnClickListener(this)
    }

    override fun onBind(item: SearchWordItem) {
        wordView.text = item.word
        remoteTranslationView.text = item.translationRemote.joinToString()
        remoteTranslationView.run {
            isVisible = item.translationRemote.isNotEmpty()
            text = item.translationRemote.joinToString()
        }
        localTranslationView.run {
            isVisible = item.translationLocal.isNotEmpty()
            text = item.translationLocal.joinToString()
        }
    }

    override fun onClick(view: View?) {
        actionClick(view, adapterPosition)
    }
}