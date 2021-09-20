package ru.nblackie.dictionary.impl.presentation.edit.recycler

import android.view.View
import com.google.android.material.textfield.TextInputEditText
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.R

/**
 * @author tatarchukilya@gmail.com
 */
internal class EditContentViewHolder(view: View) : BindViewHolder<EditContentItem>(view) {

    private val textInputEditText = view.findViewById<TextInputEditText>(R.id.text_input_view)

    override fun onBind(item: EditContentItem) {
        textInputEditText.hint = item.hint
        textInputEditText.setText(item.content)
    }
}