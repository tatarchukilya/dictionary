package ru.nblackie.dictionary.impl.presentation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.edit.recycler.EditContentViewHolder
import ru.nblackie.dictionary.impl.presentation.viewmodel.ViewModelFragment

/**
 * @author tatarchukilya@gmail.com
 */
class EditContentFragment : ViewModelFragment(R.layout.fragment_edit) {

    private val recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private inner class RecyclerAdapter : RecyclerView.Adapter<BindViewHolder<ListItem>>() {

        private val items = mutableListOf<ListItem>()

        @Suppress("UNCHECKED_CAST")
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindViewHolder<ListItem> {
            return EditContentViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_edit_content, parent, false)
            ) as BindViewHolder<ListItem>
        }

        override fun onBindViewHolder(holder: BindViewHolder<ListItem>, position: Int) {
            holder.onBind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }
}