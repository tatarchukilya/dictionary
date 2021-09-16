package ru.nblackie.dictionary.impl.presentation.search

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import com.google.android.material.transition.MaterialContainerTransform
import ru.nblackie.core.recycler.BindViewHolder
import ru.nblackie.core.recycler.ListItem
import ru.nblackie.core.utils.StartEndTransitionListener
import ru.nblackie.core.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.presentation.converter.toWordArgs
import ru.nblackie.dictionary.impl.presentation.preview.PreviewWordFragment
import ru.nblackie.dictionary.impl.presentation.search.recycler.EmptyViewHolder
import ru.nblackie.dictionary.impl.presentation.search.recycler.SingleWordViewHolder


/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchFragment : Fragment() {

    private var input: String = ""
        set(value) {
            setMenuVisibility(value.isNotEmpty())
            if (field != value) {
                field = value
                viewModel.search(value)
            }
        }
    private lateinit var viewModel: SearchViewModel
    private lateinit var editText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchToggle: LinearLayout
    private lateinit var toolbar: Toolbar
    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = resources.getColor(android.R.color.transparent, null)
            addListener(object : StartEndTransitionListener() {
                override fun onTransitionStart(transition: Transition) {
                    if (searchToggle.isVisible) {
                        searchToggle.isVisible = false
                    }
                }

                override fun onTransitionEnd(transition: Transition) {
                    if (!searchToggle.isVisible) {
                        searchToggle.isVisible = true
                    }
                }

            })
        }
        viewModel = ViewModelProvider(
            this, DictionaryFeatureHolder
                .getInternalApi()
                .searchViewModelProviderFactory()
        ).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView(view)
        setUpToolbar(view)
        setUpObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_clear_search -> {
            editText.text.clear()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpView(view: View) {
        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter
        searchToggle = view.findViewById(R.id.search_toggle)
        progressBar = view.findViewById(R.id.progressbar)
        editText = view.findViewById(R.id.input_query_view)
        editText.showKeyboard()
        editText.doAfterTextChanged { input = it.toString() }
        view.findViewById<RadioGroup>(R.id.dictionary_toggle)
            .setOnCheckedChangeListener { _, buttonId ->
                when (buttonId) {
                    R.id.personal_dictionary -> {
                        Toast.makeText(requireContext(), "Personal", Toast.LENGTH_SHORT).show()
                    }
                    R.id.general_dictionary -> {
                        Toast.makeText(requireContext(), "General", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        setMenuVisibility(editText.text.isNotEmpty())
    }

    private fun setUpObserver() {
        viewModel.words.observe(viewLifecycleOwner, { adapter.items = it })
        viewModel.progress.observe(viewLifecycleOwner, { progressBar.isVisible = it })
    }

    private inner class RecyclerAdapter() : RecyclerView.Adapter<BindViewHolder<ListItem>>() {

        var items = mutableListOf<ListItem>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        @Suppress("UNCHECKED_CAST")
        override fun onCreateViewHolder(parent: ViewGroup, type: Int): BindViewHolder<ListItem> =
            when (type) {
                EmptyItem.VIEW_TYPE -> EmptyViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_empty, parent, false)
                )
                SearchWordItem.VIEW_TYPE -> SingleWordViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_word, parent, false)
                ) { view, position ->
                    viewHolderClick(view, position)
                }
                else -> throw java.lang.IllegalArgumentException("Illegal viewType $type")
            } as BindViewHolder<ListItem>

        override fun onBindViewHolder(holder: BindViewHolder<ListItem>, position: Int) {
            holder.onBind(items[position])
        }

        override fun onViewRecycled(holder: BindViewHolder<ListItem>) {
            holder.unbind()
        }

        override fun getItemCount(): Int = items.size

        override fun getItemViewType(position: Int): Int {
            return items[position].viewType()
        }

        private fun viewHolderClick(view: View?, position: Int) {
            with(items[position] as SearchWordItem) {
                findNavController().navigate(
                    R.id.fragment_edit_word, PreviewWordFragment.createArgs(this.toWordArgs())
                )
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}