package ru.nblackie.dictionary.impl.presentation.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.nblackie.core.impl.utils.firstCharUpperCase
import ru.nblackie.core.impl.utils.getTintDrawableByAttr
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.domain.model.TranscriptionItem
import ru.nblackie.dictionary.impl.domain.model.TranslationItem
import ru.nblackie.dictionary.impl.presentation.actions.Event
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment
import ru.nblackie.dictionary.impl.presentation.preview.recycler.TranscriptionViewHolder
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
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            viewModel.addTranslation()
            showEditFragment()
        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.previewStateNew.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                activity?.let { activity ->
                    menu.findItem(R.id.item_add).icon = if (it.isAdded) {
                        activity.getTintDrawableByAttr(
                            R.drawable.ic_bookmark_24,
                            R.attr.colorSecondary
                        )
                    } else {
                        activity.getTintDrawableByAttr(
                            R.drawable.ic_bookmark_border_24,
                            android.R.attr.textColorSecondary
                        )
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.item_add -> {
            addToPersonal()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.previewStateNew.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                toolbar.title = it.word.firstCharUpperCase()
                transcriptionAdapter.item = it.transcriptions
                translationAdapter.submitList(it.translations)
            }
        }
    }

    private fun showEditFragment() {
        findNavController().navigate(R.id.preview_to_edit_dialog)
    }

    override fun selectTranslation(event: Event) {
        (event as Event.Click).run {
            viewModel.selectTranslation(position)
            showEditFragment()
        }
    }

    override fun addToPersonal() {
        viewModel.addToPersonal()
    }

    private inner class TranscriptionAdapter : RecyclerView.Adapter<TranscriptionViewHolder>() {

        var item: TranscriptionItem? = null
            set(value) {
                field = value
                notifyItemChanged(0)
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranscriptionViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.view_transcription_title, parent, false)
            return TranscriptionViewHolder(view)
        }

        override fun onBindViewHolder(holder: TranscriptionViewHolder, position: Int) {
            holder.onBind(item ?: return)
        }

        override fun getItemCount(): Int = 1
    }

    private inner class TranslationAdapter :
        ListAdapter<TranslationItem, TranslationViewHolder>(TranslationItemCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.view_translation, parent, false)
            return TranslationViewHolder(view) {
                selectTranslation(it)
            }
        }

        override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

    //DiffUtil
    private class TranslationItemCallback : DiffUtil.ItemCallback<TranslationItem>() {

        override fun areItemsTheSame(oldItem: TranslationItem, newItem: TranslationItem): Boolean {
            return oldItem.translation == newItem.translation
        }

        override fun areContentsTheSame(oldItem: TranslationItem, newItem: TranslationItem): Boolean {
            return oldItem.translation == newItem.translation
        }
    }
}