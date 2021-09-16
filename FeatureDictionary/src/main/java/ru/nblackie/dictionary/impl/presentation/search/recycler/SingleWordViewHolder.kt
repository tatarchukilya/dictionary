package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import android.widget.TextView
import ru.nblackie.core.recycler.BindViewHolder
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */
class SingleWordViewHolder(
    view: View,
    private val actionClick: (view: View?, position: Int) -> Unit
) :
    BindViewHolder<SearchWordItem>(view),
    View.OnClickListener {
    init {
        itemView.setOnClickListener(this)
    }

    private val word = itemView.findViewById<TextView>(R.id.word_text_view)
    private val translation = itemView.findViewById<TextView>(R.id.translation_text_view)

    override fun onBind(item: SearchWordItem) {
        word.text = item.word
        translation.text = item.translation.joinToString()
    }

    override fun onClick(view: View?) {
        actionClick(view, adapterPosition)
    }
}