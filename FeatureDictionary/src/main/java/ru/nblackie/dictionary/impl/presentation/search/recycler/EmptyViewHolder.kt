package ru.nblackie.dictionary.impl.presentation.search.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.nblackie.core.dpToPixels
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
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

    companion object {

        fun create(parent: ViewGroup): EmptyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_empty, parent, false)
            return EmptyViewHolder(view)
        }
    }
}