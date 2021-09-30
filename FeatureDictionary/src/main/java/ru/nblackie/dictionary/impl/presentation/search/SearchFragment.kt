package ru.nblackie.dictionary.impl.presentation.search

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.core.impl.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.presentation.search.recycler.EmptyViewHolder
import ru.nblackie.dictionary.impl.presentation.search.recycler.SingleWordViewHolder
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import android.view.ViewTreeObserver.OnGlobalLayoutListener as OnGlobalLayoutListener1

/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchFragment : ViewModelFragment(R.layout.fragment_search) {

    private var input: String = ""
        set(value) {
            value.isNotEmpty().let {
                setMenuVisibility(it)
                toggleState.setVisibility(it)
            }
            if (field != value) {
                field = value
                viewModel.search(value)
            }
        }
    // TODO сохранеие input в arguments

    private val toggleState = SearchToggleState()
    private lateinit var searchView: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchToggle: LinearLayout
    private lateinit var toolbar: Toolbar
    private val adapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = resources.getColor(android.R.color.transparent, activity?.theme)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpView(view)
        setUpToolbar(view)
        setUpObserver()
    }

    override fun onResume() {
        super.onResume()
        searchView.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        searchView.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_clear_search -> {
            searchView.text.clear()
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
        searchToggle.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener1 {
            override fun onGlobalLayout() {
                if (input.isEmpty()) { // Если слово поиска пустое, скрыть [searchToggle]
                    searchToggle.translationY = -searchToggle.height.toFloat()
                }
                searchToggle.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        progressBar = view.findViewById(R.id.progressbar)
        searchView = view.findViewById(R.id.input_query_view)
        searchView.showKeyboard()
        searchView.doAfterTextChanged { input = it.toString() }
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
        setMenuVisibility(searchView.text.isNotEmpty())
    }

    private fun setUpObserver() {
        viewModel.searchResult.observe(viewLifecycleOwner, { adapter.submitList(it) })
        viewModel.progressSearch.observe(viewLifecycleOwner, { progressBar.isVisible = it })
    }

    private inner class RecyclerAdapter() :
        ListAdapter<ListItem, BindViewHolder<ListItem>>(ListItemCallback()) {

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
            holder.onBind(getItem(position))
        }

        override fun onViewRecycled(holder: BindViewHolder<ListItem>) {
            holder.unbind()
        }

        override fun getItemViewType(position: Int): Int {
            return getItem(position).viewType()
        }

        private fun viewHolderClick(view: View?, position: Int) {
            viewModel.select(position)
            findNavController().navigate(R.id.fragment_preview)
        }
    }

    private class ListItemCallback : DiffUtil.ItemCallback<ListItem>() {

        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            if (oldItem.viewType() == newItem.viewType()) {
                return when (oldItem) {
                    is SearchWordItem -> {
                        oldItem.word == (newItem as SearchWordItem).word
                    }
                    else -> true
                }
            }
            return false
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return if (oldItem is EmptyItem) {
                true
            } else {
                (oldItem as SearchWordItem) == newItem as SearchWordItem
            }
        }
    }

    // Класс, для анимации появления и скрытия searchToggle
    private inner class SearchToggleState {
        private val showAnimator = ValueAnimator().apply {
            duration = 300L
            addUpdateListener {
                searchToggle.translationY = it.animatedValue as Float
            }
        }

        private val hideAnimator = ValueAnimator().apply {
            duration = 300L
            addUpdateListener {
                searchToggle.translationY = it.animatedValue as Float
            }
        }

        fun setVisibility(isVisible: Boolean) {
            if (isVisible) showSearchToggle() else hideSearchToggle()
        }

        private fun showSearchToggle() {
            if (searchToggle.translationY == 0f || showAnimator.isRunning) return
            hideAnimator.cancel()
            with(showAnimator) {
                val from = searchToggle.height - (searchToggle.height - searchToggle.translationY)
                setFloatValues(from, 0f)
                start()
            }
        }

        private fun hideSearchToggle() {
            if (searchToggle.translationY == -searchToggle.height.toFloat() || hideAnimator.isRunning) {
                return
            }
            showAnimator.cancel()
            with(hideAnimator) {
                val from = searchToggle.height - (searchToggle.height - searchToggle.translationY)
                setFloatValues(from, -searchToggle.height.toFloat())
                start()
            }
        }
    }
}