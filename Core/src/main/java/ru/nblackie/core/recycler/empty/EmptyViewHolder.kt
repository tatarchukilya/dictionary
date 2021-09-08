package ru.nblackie.core.recycler.empty

import android.view.View
import ru.nblackie.core.dpToPixels
import ru.nblackie.core.recycler.BindViewHolder

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