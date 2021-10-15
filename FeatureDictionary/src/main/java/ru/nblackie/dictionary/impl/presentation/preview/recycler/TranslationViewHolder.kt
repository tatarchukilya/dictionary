package ru.nblackie.dictionary.impl.presentation.preview.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.core.Add
import ru.nblackie.dictionary.impl.presentation.core.SelectTranslation

/**
 * @author tatarchukilya@gmail.com
 */
internal class TranslationViewHolder(view: View, val action: (Action) -> Unit) : BindViewHolder<TranslationItem>(view) {

    private val translationView = view.findViewById<TextView>(R.id.translation)
    private val addExerciseButton = view.findViewById<ImageView>(R.id.add_exercise_image)

    init {
        view.setOnClickListener {
            action(SelectTranslation(adapterPosition))
        }
        addExerciseButton.setOnClickListener {
            action(Add(adapterPosition))
        }
    }

    override fun onBind(item: TranslationItem) {
        translationView.text = item.translation
        val icon = if (item.isAdded) {
            itemView.context.getTintDrawableByAttr(R.drawable.ic_bookmark_24, R.attr.colorSecondary)
        } else {
            itemView.context.getTintDrawableByAttr(R.drawable.ic_bookmark_border_24, android.R.attr.textColorSecondary)
        }
        addExerciseButton.setImageDrawable(icon)
    }
}