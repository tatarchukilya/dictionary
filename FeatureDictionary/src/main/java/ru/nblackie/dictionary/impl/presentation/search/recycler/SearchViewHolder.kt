package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import android.widget.TextView
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder
import ru.nblackie.dictionary.impl.presentation.core.SelectWord

/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchViewHolder(view: View, actionObserver: (Action) -> Unit) :
    BindViewHolder<SearchItem>(view, actionObserver) {

    private val wordView = view.findViewById<TextView>(R.id.word_text_view)
    private val translationView = view.findViewById<TextView>(R.id.local_translation)

    init {
        view.setOnClickListener {
            actionObserver.invoke(SelectWord(bindingAdapterPosition))
        }
    }

    override fun onBind(item: SearchItem) {
        wordView.text = item.word
        translationView.text = item.joinString
    }
}