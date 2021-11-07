package ru.nblackie.dictionary.impl.presentation.dictionary

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.core.BindViewHolder
import ru.nblackie.dictionary.impl.presentation.core.Event
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.recycler.callback.DictionaryItemCallback
import ru.nblackie.dictionary.impl.presentation.recycler.items.TypedItem
import ru.nblackie.dictionary.impl.presentation.recycler.viewholder.dictionaryViewHolderFactory

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryFragment : ViewModelFragment(R.layout.fragment_dictionary), DictionaryView {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private val adapter = RecyclerAdapter(DictionaryItemCallback())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
        setUbObserver()
    }

    private fun setUpToolbar(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        toolbar = view.findViewById<Toolbar>(R.id.toolbar).apply {
            navigationIcon =
                AppCompatResources.getDrawable(context, R.drawable.ic_search_24)
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setOnClickListener(searchClickListener)
        toolbar.setNavigationOnClickListener(searchClickListener)
    }

    private fun setUbObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dictionaryState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                adapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dictionaryEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is Event.ShowSearch -> showSearch()
                    is Event.ShowDetail -> showDetail()
                    else -> Log.i("<>", "Illegal event $it")
                }
            }
        }
    }

    private val searchClickListener: View.OnClickListener = View.OnClickListener {
        clickSearch()
    }

    private inner class RecyclerAdapter(callback: DictionaryItemCallback) :
        ListAdapter<TypedItem, BindViewHolder<TypedItem>>(callback) {

        override fun onCreateViewHolder(parent: ViewGroup, type: Int): BindViewHolder<TypedItem> =
            dictionaryViewHolderFactory(parent, type) {
                selectWord(it)
            }

        override fun onBindViewHolder(holder: BindViewHolder<TypedItem>, position: Int) {
            holder.onBind(getItem(position))
        }

        override fun getItemViewType(position: Int): Int {
            return getItem(position).type.code
        }
    }

    override fun setItem(items: List<TypedItem>) {
        adapter.submitList(items)
    }

    override fun selectWord(action: Action) {
        viewModel.handleAction(action)
    }

    override fun clickSearch() {
        viewModel.handleAction(Action.ShowSearch)
    }

    override fun showDetail() {
        findNavController().navigate(R.id.fragment_detail)
    }

    override fun showSearch() {
        val extras =
            FragmentNavigatorExtras(toolbar to getString(R.string.dictionary_search_transition))
        findNavController().navigate(R.id.fragment_search, null, null, extras)
    }
}