package ru.nblackie.dictionary.impl.presentation.preview.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.actions.Event

/**
 * @author tatarchukilya@gmail.com
 */
internal class TranslationViewHolder(view: View, val click: (Event) -> Unit) : BindViewHolder<TranslationItem>(view) {

    private val translationView = view.findViewById<TextView>(R.id.translation)
    private val addExerciseButton = view.findViewById<ImageView>(R.id.add_exercise_image)

    init {
        view.setOnClickListener {
            click(Event.Click(adapterPosition))
        }
    }

    override fun onBind(item: TranslationItem) {
        translationView.text = item.translation
        addExerciseButton.setImageDrawable(
            itemView.context.getTintDrawableByAttr(
                R.drawable.ic_check_24, if (item.isAdded) R.attr.colorSecondary else android.R.attr.textColorSecondary
            )
        )
    }
}