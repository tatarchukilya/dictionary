package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.View
import ru.nblackie.core.dpToPixels
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.dictionary.impl.domain.model.EmptyItem

/**
 * @author tatarchukilya@gmail.com
 */
class EmptyViewHolder(view: View) : BindViewHolder<EmptyItem>(view) {

    override fun onBind(item: EmptyItem) {
        with(itemView) {
            val newHeight = item.heightDp.dpToPixels(itemView.context).toInt()
            if (layoutParams.height != newHeight) {
                layoutParams.height = newHeight
                requestLayout()
            }
        }
    }
}