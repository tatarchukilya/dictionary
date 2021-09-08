package ru.nblackie.dictionary.impl.presentation.search

import android.animation.Animator
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import ru.nblackie.core.recycler.RecyclerAdapter
import ru.nblackie.core.utils.EndAnimatorListener
import ru.nblackie.core.utils.EndTransitionListener
import ru.nblackie.core.utils.StartEndTransitionListener
import ru.nblackie.core.utils.showKeyboard
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.dictionary.impl.presentation.edit.EditWordFragment
import ru.nblackie.dictionary.impl.presentation.search.recycler.viewHolderFactoryMethod


/**
 * @author tatarchukilya@gmail.com
 */
internal class SearchFragment : Fragment() {

    private var input: String = ""
        set(value) {
            setMenuVisibility(value.isNotEmpty())
            //searchToggle.isVisible = value.isNotEmpty()
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
    private val adapter = RecyclerAdapter { parent: ViewGroup, viewType: Int ->
        viewHolderFactoryMethod(parent, viewType)
    }

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
        toolbar = view.findViewById<Toolbar>(R.id.toolbar).apply {
            navigationIcon =
                AppCompatResources.getDrawable(context, R.drawable.ic_arrow_back_24)
        }
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
        viewModel.editWord.observe(viewLifecycleOwner, {
            findNavController().navigate(
                R.id.fragment_edit_word, EditWordFragment.createArgs(
                    it.word, it.transcription, ArrayList(it.translation)
                )
            )
        })
    }

    private fun showSearchToggle() {
//        if (!searchToggle.isVisible()) {
//            with(searchToggle) {
//                visibility = View.VISIBLE
//                animate()
//                    .translationY((toolbar.bottom - searchToggle.top).toFloat())
//                    .setDuration(300)
//                    .start()
//            }
//        }
        TransitionManager.beginDelayedTransition(
            requireView() as ViewGroup,
            Slide(Gravity.TOP).apply {
                duration = 300
                addTarget(R.id.search_toggle)
                addListener(object : EndTransitionListener() {
                    override fun onTransitionEnd(transition: Transition) {
                        searchToggle.visibility = View.VISIBLE
                    }
                })
            })
    }

    private fun hideSearchToggle() {
        with(searchToggle) {
            animate()
                .translationY((toolbar.bottom - searchToggle.bottom).toFloat())
                .setDuration(300)
                .setListener(object : EndAnimatorListener() {
                    override fun onAnimationEnd(p0: Animator?) {
                        visibility = View.GONE
                    }
                })
                .start()
        }
    }


    private fun toggleVisible(isVisible: Boolean) {

    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}