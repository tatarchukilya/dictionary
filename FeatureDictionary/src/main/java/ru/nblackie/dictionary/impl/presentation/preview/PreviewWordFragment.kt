package ru.nblackie.dictionary.impl.presentation.preview

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.nblackie.core.recycler.BindViewHolder
import ru.nblackie.core.recycler.ListItem
import ru.nblackie.core.utils.firstCharUpperCase
import ru.nblackie.core.utils.getPrimaryTextColor
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.presentation.data.WordArgs
import ru.nblackie.dictionary.impl.presentation.preview.recycler.PreviewDataItem
import ru.nblackie.dictionary.impl.presentation.preview.recycler.PreviewDataViewHolder
import androidx.core.graphics.drawable.DrawableCompat
import ru.nblackie.core.utils.getTintDrawableByAttr


/**
 * @author tatarchukilya@gmail.com
 */
class PreviewWordFragment : Fragment() {

    private var viewModel: PreviewWordViewModel? = null

    private lateinit var recyclerView: RecyclerView

    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //TODO add share transition
        viewModel = ViewModelProvider(
            this, DictionaryFeatureHolder
                .getInternalApi()
                .editWordViewModelProviderFactory()
        ).get(PreviewWordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_preview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments().getParcelable<WordArgs>(WORD_KEY)
        setUpToolbar(view, args?.word)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter

        viewModel?.setData(
            getString(R.string.translation), args?.translation,
            getString(R.string.transcription), args?.transcription
        )
        viewModel?.items?.observe(viewLifecycleOwner, {
            adapter.items = it
        })
    }

    private fun setUpToolbar(view: View, title: String?) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = title?.firstCharUpperCase()
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_preview, menu)
        viewModel?.isAdded?.observe(viewLifecycleOwner, { isAdded ->
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
            viewModel?.add()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun tintMenuIcon(item: MenuItem, color: Int) {
        val wrapDrawable = DrawableCompat.wrap(item.icon)
        activity?.let {
            val col = it.getPrimaryTextColor(color)
            DrawableCompat.setTint(wrapDrawable, col)
        }
        item.icon = wrapDrawable
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

    companion object {

        private const val WORD_KEY = "word_arg"

        fun createArgs(arg: WordArgs) =
            Bundle().apply {
                putParcelable(WORD_KEY, arg)
            }
    }
}