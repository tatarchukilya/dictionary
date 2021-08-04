package ru.nblackie.core.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author tatarchukilya@gmail.com
 */
abstract class BindViewHolder<I : ListItem>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun onBind(item: I)

    open fun unbind() {

    }
}