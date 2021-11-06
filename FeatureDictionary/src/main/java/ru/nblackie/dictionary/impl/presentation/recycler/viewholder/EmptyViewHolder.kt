package ru.nblackie.dictionary.impl.presentation.recycler.viewholder

import android.view.View
import ru.nblackie.core.dpToPixels
import ru.nblackie.dictionary.impl.presentation.recycler.items.EmptyItem
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal class EmptyViewHolder(view: View) : BindViewHolder<EmptyItem>(view) {

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