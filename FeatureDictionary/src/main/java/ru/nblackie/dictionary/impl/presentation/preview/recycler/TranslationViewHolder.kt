package ru.nblackie.dictionary.impl.presentation.preview.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.edit.EditContentFragment

/**
 * @author tatarchukilya@gmail.com
 */
class TranslationViewHolder(view: View) : BindViewHolder<TranslationItem>(view) {

    private val translationView = view.findViewById<TextView>(R.id.translation)
    private val addExerciseButton = view.findViewById<ImageView>(R.id.add_exercise_image)

    init {
        view.setOnClickListener {
            val extras = FragmentNavigatorExtras(translationView to translationView.transitionName)
            it.findNavController().navigate(
                R.id.preview_to_edit, EditContentFragment.createArgs(adapterPosition), null, extras
            )
        }
    }

    override fun onBind(item: TranslationItem) {
        translationView.transitionName = EditContentFragment.getTransitionName(adapterPosition)
        translationView.text = item.translation
        addExerciseButton.setImageResource(
            if (item.isAdded) R.drawable.ic_check_circle_24 else R.drawable.ic_check_circle_outline_24
        )
    }
}