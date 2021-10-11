package ru.nblackie.dictionary.impl.presentation.preview.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.actions.Action

/**
 * @author tatarchukilya@gmail.com
 */
internal class TranslationViewHolder(view: View, val click: (Action) -> Unit) : BindViewHolder<TranslationItem>(view) {

    private val translationView = view.findViewById<TextView>(R.id.translation)
    private val addExerciseButton = view.findViewById<ImageView>(R.id.add_exercise_image)

    init {
        view.setOnClickListener {
            click(Action.Click(adapterPosition, translationView))
        }
    }

    override fun onBind(item: TranslationItem) {

        translationView.text = item.translation
        addExerciseButton.setImageResource(
            if (item.isAdded) R.drawable.ic_check_circle_24 else R.drawable.ic_check_circle_outline_24
        )
    }
}