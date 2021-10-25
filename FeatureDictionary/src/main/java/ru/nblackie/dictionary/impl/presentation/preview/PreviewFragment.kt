package ru.nblackie.dictionary.impl.presentation.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
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
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.core.AddTranslation
import ru.nblackie.dictionary.impl.presentation.core.MatchTranslation
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranscriptionViewHolder
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranslationItemCallback
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranslationViewHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal class PreviewFragment : ViewModelFragment(R.layout.fragment_preview), PreviewView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton

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
            viewModel.previewState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                setState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.previewEvent.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                showNewTranslationView()
            }
        }
    }

    private inner class TranscriptionAdapter : RecyclerView.Adapter<TranscriptionViewHolder>() {

        var items: List<TranscriptionItem> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
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

    override fun setState(state: SharedViewModel.PreviewState) {
        toolbar.title = state.word.firstCharUpperCase()
        transcriptionAdapter.items = state.transcriptions
        translationAdapter.submitList(state.translations)
        //TODO придумать архитектурное решение
        if (state.translations.isEmpty()) {
            activity?.onBackPressed()
        }
    }

    override fun matchTranslation(action: MatchTranslation) {
        viewModel.handleAction(action)
    }

    override fun sendNewWordAction() {
        viewModel.handleAction(AddTranslation)
    }

    override fun showNewTranslationView() {
        findNavController().navigate(R.id.preview_to_edit_dialog)
    }
}