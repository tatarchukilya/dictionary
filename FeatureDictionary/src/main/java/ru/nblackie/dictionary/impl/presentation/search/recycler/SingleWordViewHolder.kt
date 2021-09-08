package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import android.widget.TextView
import ru.nblackie.core.recycler.BindViewHolder
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
class SingleWordViewHolder(view: View) : BindViewHolder<SingleWordItem>(view),
    View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    private val word = itemView.findViewById<TextView>(R.id.word_text_view)
    private val translation = itemView.findViewById<TextView>(R.id.translation_text_view)
    private lateinit var click: () -> Unit

    override fun onBind(item: SingleWordItem) {
        word.text = item.word
        translation.text = item.translation
        click = item.click
    }

    override fun onClick(p0: View?) {
        click()
    }
}