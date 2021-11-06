package ru.nblackie.dictionary.impl.presentation.recycler.viewholder

import android.view.View
import android.widget.TextView
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder
import ru.nblackie.dictionary.impl.presentation.recycler.items.DictionaryItem

/**
 * @author Ilya Tatarchuk
 */
internal class DictionaryViewHolder(view: View, actionObserver: (Action) -> Unit) :
    BindViewHolder<DictionaryItem>(view, actionObserver) {

    private val wordView = view.findViewById<TextView>(R.id.word_text_view)
    private val translationView = view.findViewById<TextView>(R.id.local_translation)

    init {
        view.setOnClickListener {
            actionObserver.invoke(Action.DictionarySelect(bindingAdapterPosition))
        }
    }

    override fun onBind(item: DictionaryItem) {
        wordView.text = item.word
        translationView.text = item.joinString
    }
}