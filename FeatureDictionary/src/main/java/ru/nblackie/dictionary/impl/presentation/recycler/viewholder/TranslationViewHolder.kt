package ru.nblackie.dictionary.impl.presentation.recycler.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranslationItem

/**
 * @author tatarchukilya@gmail.com
 */
internal class TranslationViewHolder(view: View, val action: (Action.MatchTranslation) -> Unit) : BindViewHolder<TranslationItem>(view) {

    private val translationView = view.findViewById<TextView>(R.id.translation)
    private val addButton = view.findViewById<ImageView>(R.id.add_translation)

    init {
        addButton.setOnClickListener {
            action(Action.MatchTranslation(adapterPosition))
        }
    }

    override fun onBind(item: TranslationItem) {
        translationView.text = item.translation.data
        val icon = if (item.translation.isAdded) {
            itemView.context.getTintDrawableByAttr(R.drawable.ic_bookmark_24, R.attr.colorSecondary)
        } else {
            itemView.context.getTintDrawableByAttr(R.drawable.ic_bookmark_border_24, android.R.attr.textColorSecondary)
        }
        addButton.setImageDrawable(icon)
    }
}