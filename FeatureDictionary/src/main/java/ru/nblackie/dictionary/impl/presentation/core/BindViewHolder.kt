package ru.nblackie.dictionary.impl.presentation.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem

/**
 * @author Ilya Tatarchuk
 */
internal abstract class BindViewHolder<I : TypedItem>(
    view: View,
    var actionObserver: ((Action) -> Unit)? = null
) : RecyclerView.ViewHolder(view) {

    abstract fun onBind(item: I)
}