package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import android.widget.TextView
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

    private var data: SearchWordItem? = null

    private val wordView = itemView.findViewById<TextView>(R.id.word_text_view)
    private val translationView = itemView.findViewById<TextView>(R.id.translation_text_view)

    init {
        wordView.transitionName = itemView.context.getString(R.string.search_preview_transition)
        itemView.setOnClickListener(this)
    }

    override fun onBind(item: SearchWordItem) {
        data = item
        wordView.text = item.word
        translationView.text = item.translation.joinToString()
    }

    override fun unbind() {
        data = null
    }

    override fun onClick(view: View?) {
        actionClick(view, adapterPosition)
    }
}