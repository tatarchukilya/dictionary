package ru.nblackie.dictionary.impl.presentation.preview

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.core.impl.utils.firstCharUpperCase
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.domain.model.PreviewDataItem
import ru.nblackie.dictionary.impl.presentation.preview.recycler.PreviewDataViewHolder
import ru.nblackie.dictionary.impl.presentation.viewmodel.SharedViewModel
import ru.nblackie.dictionary.impl.presentation.viewmodel.ViewModelFragment


/**
 * @author tatarchukilya@gmail.com
 */
class PreviewFragment : ViewModelFragment(R.layout.fragment_preview) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        setUpObserver()
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_preview, menu)
        viewModel.isAdded.observe(viewLifecycleOwner, { isAdded ->
            activity?.let {
                menu.findItem(R.id.item_add).icon = if (isAdded) {
                    it.getTintDrawableByAttr(R.drawable.ic_bookmark_24, android.R.attr.colorPrimary)
                } else {
                    it.getTintDrawableByAttr(
                        R.drawable.ic_bookmark_border_24,
                        android.R.attr.textColorSecondary
                    )
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_add -> {
            viewModel.add()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpObserver() {
        viewModel.selectedWord.observe(viewLifecycleOwner, {
            toolbar.title = it.firstCharUpperCase()
        })
        viewModel.selectedWordData.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    private inner class RecyclerAdapter() : RecyclerView.Adapter<BindViewHolder<ListItem>>() {

        var items = mutableListOf<ListItem>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        @Suppress("UNCHECKED_CAST")
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindViewHolder<ListItem> = when (viewType) {
            PreviewDataItem.VIEW_TYPE -> PreviewDataViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_bind_content, parent, false)
            )
            else -> throw java.lang.IllegalArgumentException("Illegal viewType $viewType")
        } as BindViewHolder<ListItem>

        override fun onBindViewHolder(holder: BindViewHolder<ListItem>, position: Int) {
            holder.onBind(items[position])
        }

        override fun onViewRecycled(holder: BindViewHolder<ListItem>) {
            holder.unbind()
        }

        override fun getItemCount(): Int = items.size

        override fun getItemViewType(position: Int): Int = items[position].viewType()
    }
}