package ru.nblackie.dictionary.impl.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.utils.firstCharUpperCase
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.Action
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranscriptionItem
import ru.nblackie.dictionary.impl.presentation.recycler.items.TranslationItem
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.recycler.viewholder.TranscriptionViewHolder
import ru.nblackie.dictionary.impl.presentation.recycler.callback.TranslationItemCallback
import ru.nblackie.dictionary.impl.presentation.recycler.viewholder.TranslationViewHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal class DetailFragment : ViewModelFragment(R.layout.fragment_detail), DetailView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private val transcriptionAdapter = TranscriptionAdapter()
    private val translationAdapter = TranslationAdapter()
    private val concatAdapter = ConcatAdapter(
        //ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
        transcriptionAdapter, translationAdapter
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = concatAdapter
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            sendNewWordAction()
        }
        setUpObserver()
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                setState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.detailEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                showNewTranslationView()
            }
        }
    }

    private inner class TranscriptionAdapter : RecyclerView.Adapter<TranscriptionViewHolder>() {

        var items: List<TranscriptionItem> = listOf()
            @SuppressLint("NotifyDataSetChanged")
            set(value) {
                field = value
                notifyDataSetChanged() //Список всегда из одного элемента, поэтому notifyDataSetChanged норм
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranscriptionViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.view_transcription_title, parent, false)
            return TranscriptionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TranscriptionViewHolder, position: Int) {
            holder.onBind(items[position])
        }

        override fun getItemCount(): Int = items.size
    }

    private inner class TranslationAdapter :
        ListAdapter<TranslationItem, TranslationViewHolder>(TranslationItemCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.view_translation, parent, false)
            return TranslationViewHolder(view) { matchTranslation(it) }
        }

        override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

    override fun setState(state: SharedViewModel.DetailState) {
        toolbar.title = state.word.firstCharUpperCase()
        transcriptionAdapter.items = state.transcriptions
        translationAdapter.submitList(state.translations)
        //TODO придумать архитектурное решение
        if (state.translations.isEmpty()) {
            activity?.onBackPressed()
        }
    }

    override fun matchTranslation(action: Action.MatchTranslation) {
        viewModel.handleAction(action)
    }

    override fun sendNewWordAction() {
        viewModel.handleAction(Action.AddTranslation)
    }

    override fun showNewTranslationView() {
        findNavController().navigate(R.id.detail_to_edit_dialog)
    }
}