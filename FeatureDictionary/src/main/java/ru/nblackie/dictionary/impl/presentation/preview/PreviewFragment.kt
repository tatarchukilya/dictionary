package ru.nblackie.dictionary.impl.presentation.preview

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.core.impl.utils.firstCharUpperCase
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranscriptionViewHolder
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranslationViewHolder
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment


/**
 * @author tatarchukilya@gmail.com
 */
internal class PreviewFragment : ViewModelFragment(R.layout.fragment_preview) {

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
        postponeEnterTransition()
        recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
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
        viewModel.selectedItem.observe(viewLifecycleOwner, { data ->
            activity?.let {
                menu.findItem(R.id.item_add).icon = if (data.isAdded) {
                    it.getTintDrawableByAttr(
                        R.drawable.ic_bookmark_24,
                        R.attr.colorSecondary
                    )
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
        viewModel.selectedItem.observe(viewLifecycleOwner, {
            toolbar.title = it.word.firstCharUpperCase()
            adapter.items = it.items.toMutableList()
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
            R.layout.view_transcription_title -> {
                TranscriptionViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_transcription_title, parent, false)
                )
            }
            R.layout.view_translation -> {
                TranslationViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_translation, parent, false)
                )
            }
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

        private fun clickAction(transitionView: View, position: Int) {

        }
    }
}