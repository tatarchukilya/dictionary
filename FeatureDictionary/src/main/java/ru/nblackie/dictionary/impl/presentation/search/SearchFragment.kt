package ru.nblackie.dictionary.impl.presentation.search

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.recycler.BindViewHolder
import ru.nblackie.core.impl.recycler.ListItem
import ru.nblackie.core.impl.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.EmptyItem
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.search.recycler.EmptyViewHolder
import ru.nblackie.dictionary.impl.presentation.search.recycler.SingleWordViewHolder
import android.view.ViewTreeObserver.OnGlobalLayoutListener as OnGlobalLayoutListener1

/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchFragment : ViewModelFragment(R.layout.fragment_search), SearchView {

    private val toggleState = SearchToggleState()
    private lateinit var searchView: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchSwitchView: LinearLayout
    private lateinit var searchRadioGroup: RadioGroup
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_clear_search -> {
            clearSearch()
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
        searchSwitchView = view.findViewById(R.id.search_toggle)
        searchSwitchView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener1 {
            override fun onGlobalLayout() {
                if (searchView.text.isEmpty()) {
                    hideSwitchNow()
                }
                searchSwitchView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        progressBar = view.findViewById(R.id.progressbar)
        searchView = view.findViewById(R.id.input_query_view)
        searchView.showKeyboard()
        searchView.doAfterTextChanged {
            viewModel.setInput(it.toString())
        }
        searchRadioGroup = view.findViewById(R.id.dictionary_toggle)
        searchRadioGroup.setOnCheckedChangeListener { _, buttonId ->
            switchSearch(buttonId == R.id.personal)
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                if (it.input != searchView.text.toString()) {
                    searchView.setText(it.input)
                }
                setMenuVisibility(it.isClearable)
                setSwitchVisibility(it.isSwitchable)
                adapter.submitList(it.items)
                searchRadioGroup.setState(it.isCache)
            }
        }
    }

    private inner class RecyclerAdapter : ListAdapter<ListItem, BindViewHolder<ListItem>>(ListItemCallback()) {

        @Suppress("UNCHECKED_CAST")
        override fun onCreateViewHolder(parent: ViewGroup, type: Int): BindViewHolder<ListItem> =
            when (type) {
                EmptyItem.VIEW_TYPE -> EmptyViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.view_empty, parent, false)
                )
                SearchWordItem.VIEW_TYPE -> SingleWordViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.view_search_word, parent, false)
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
            select(position)
        }
    }

    //DiffUtil
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

    private fun RadioGroup.setState(isPersonal: Boolean) {
        if (checkedRadioButtonId == R.id.general && isPersonal) {
            check(R.id.personal)
        } else if (checkedRadioButtonId == R.id.personal && !isPersonal) {
            check(R.id.general)
        }
    }

    override fun setItems(items: List<ListItem>) {
        adapter.submitList(items)
    }

    override fun progressVisibility(isVisible: Boolean) {
        progressBar.isVisible = isVisible
    }

    override fun clearSearch() {
        searchView.text.clear()
    }

    override fun select(position: Int) {
        viewModel.select(position)
        findNavController().navigate(R.id.fragment_preview)
    }

    override fun search(input: String) {
        viewModel.setInput(input)
    }

    override fun switchSearch(isLocale: Boolean) {
        viewModel.switchSearch(isLocale)
    }

    override fun hideSwitchNow() {
        searchSwitchView.hideUpNow()
    }

    override fun setSwitchVisibility(isVisible: Boolean) {
        toggleState.setSearchSwitchVisibility(isVisible)
    }

    override fun setProgressVisibility(isVisible: Boolean) {
        progressBar.isVisible = isVisible
    }

    private fun View.hideUpNow() {
        translationY = -height.toFloat()
    }

    // Класс, для анимации появления и скрытия searchToggle. Какой-то костыль
    private inner class SearchToggleState {
        private val showAnimator = ValueAnimator().apply {
            duration = 300L
            addUpdateListener {
                searchSwitchView.translationY = it.animatedValue as Float
            }
        }

        private val hideAnimator = ValueAnimator().apply {
            duration = 300L
            addUpdateListener {
                searchSwitchView.translationY = it.animatedValue as Float
            }
        }

        fun setSearchSwitchVisibility(isVisible: Boolean) {
            if (isVisible) showSearchSwitch() else hideSearchSwitch()
        }

        private fun showSearchSwitch() {
            if (searchSwitchView.translationY == 0f || showAnimator.isRunning) return
            hideAnimator.cancel()
            with(showAnimator) {
                val from = searchSwitchView.height - (searchSwitchView.height - searchSwitchView.translationY)
                setFloatValues(from, 0f)
                start()
            }
        }

        private fun hideSearchSwitch() {
            if (searchSwitchView.translationY == -searchSwitchView.height.toFloat() || hideAnimator.isRunning) {
                return
            }
            showAnimator.cancel()
            with(hideAnimator) {
                val from = searchSwitchView.height - (searchSwitchView.height - searchSwitchView.translationY)
                setFloatValues(from, -searchSwitchView.height.toFloat())
                start()
            }
        }
    }
}