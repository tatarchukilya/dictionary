package ru.nblackie.dictionary.impl.presentation.search

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.Action.ClearSearch
import ru.nblackie.dictionary.impl.presentation.core.Action.SearchInput
import ru.nblackie.dictionary.impl.presentation.core.Action.SearchSelect
import ru.nblackie.dictionary.impl.presentation.core.Action.SwitchSearch
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder
import ru.nblackie.dictionary.impl.presentation.core.Event
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.SearchState
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.recycler.callback.SearchItemCallback
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem
import ru.nblackie.dictionary.impl.presentation.recycler.viewholder.searchViewHolderFactory
import android.view.ViewTreeObserver.OnGlobalLayoutListener as OnGlobalLayoutListener1

/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchFragment : ViewModelFragment(R.layout.fragment_search), SearchView {

    private val toggleState = SearchToggleState()
    private val adapter = RecyclerAdapter(SearchItemCallback())
    private var switchFlag = false

    private lateinit var searchView: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchSwitchView: LinearLayout
    private lateinit var searchRadioGroup: RadioGroup
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var remoteRadioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("<>", "onCreate")
        setHasOptionsMenu(true)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = resources.getColor(android.R.color.transparent, activity?.theme)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i("<>", "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("<>", "onViewCreated")
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

    override fun setState(state: SearchState) {
        if (state.input != searchView.text.toString()) {
            searchView.setText(state.input)
        }
        setMenuVisibility(state.isClearable)
        toggleState.setVisibility(state.isSwitchable)
        adapter.submitList(state.items)
        setSearchState(state.isCache)
        progressBar.isVisible = state.inProgress
        setCount(state.count)
    }

    override fun clearSearch() {
        viewModel.handleAction(ClearSearch)
    }

    override fun select(action: SearchSelect) {
        viewModel.handleAction(action)
    }

    override fun search(action: SearchInput) {
        viewModel.handleAction(action)
    }

    override fun switchSearch(isLocal: Boolean) {
        viewModel.handleAction(SwitchSearch(isLocal))
    }

    override fun showPreview() {
        findNavController().navigate(R.id.fragment_detail)
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpView(view: View) {
        remoteRadioButton = view.findViewById(R.id.general)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            }
        })

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
            search(SearchInput(it.toString()))
        }

        searchRadioGroup = view.findViewById(R.id.dictionary_toggle)
        if (searchRadioGroup.checkedRadioButtonId == -1) {
            switchFlag = true
        }
        searchRadioGroup.setOnCheckedChangeListener { _, id ->
            if (switchFlag) {
                switchFlag = false
                return@setOnCheckedChangeListener
            }
            when (id) {
                R.id.personal -> switchSearch(true)
                R.id.general -> switchSearch(false)
            }
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                setState(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                if (it is Event.ShowDetail) {
                    showPreview()
                }
            }
        }
    }

    private fun hideSwitchNow() {
        searchSwitchView.hideUpNow()
    }

    private fun View.hideUpNow() {
        translationY = -height.toFloat()
    }

    private fun setSearchState(isLocal: Boolean) {
        if (isLocal) switchToLocal() else switchToRemote()
    }

    private fun switchToRemote() {
        if (searchRadioGroup.checkedRadioButtonId != R.id.general) {
            searchRadioGroup.check(R.id.general)
        }
    }

    private fun switchToLocal() {
        if (searchRadioGroup.checkedRadioButtonId != R.id.personal) {
            searchRadioGroup.check(R.id.personal)
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun setCount(count: Int) {
        remoteRadioButton.text = getString(R.string.word_count, count)
    }

    private inner class RecyclerAdapter(callback: SearchItemCallback) :
        ListAdapter<TypedItem, BindViewHolder<TypedItem>>(callback) {

        override fun onCreateViewHolder(parent: ViewGroup, type: Int): BindViewHolder<TypedItem> =
            searchViewHolderFactory(parent, type) {
                viewModel.handleAction(it)
            }

        override fun onBindViewHolder(holder: BindViewHolder<TypedItem>, position: Int) {
            holder.onBind(getItem(position))
        }

        override fun getItemViewType(position: Int): Int {
            return getItem(position).type.code
        }
    }

    // Класс, для анимации появления и скрытия searchToggle. Костыль
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

        fun setVisibility(isVisible: Boolean) {
            if (isVisible) show() else hide()
        }

        private fun show() {
            if (searchSwitchView.translationY == 0f || showAnimator.isRunning) return
            hideAnimator.cancel()
            with(showAnimator) {
                val from = searchSwitchView.height - (searchSwitchView.height - searchSwitchView.translationY)
                setFloatValues(from, 0f)
                start()
            }
        }

        private fun hide() {
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