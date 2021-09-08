package ru.nblackie.dictionary.impl.presentation.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryFragment : Fragment() {

    private lateinit var viewModel: DictionaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this, DictionaryFeatureHolder.getInternalApi()
                .dictionaryViewModelProviderFactory()
        ).get(DictionaryViewModel::class.java)
//        val vm: DictionaryViewModelNew = ViewModelProvider(
//            this,
//            DictionaryFeatureHolder.getInternalApi().dictionaryViewModelCreator()
//                .create(this, Bundle())
//        ).get(DictionaryViewModelNew::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_dictionary, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
    }

    private fun setUpToolbar(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar).apply {
            navigationIcon =
                AppCompatResources.getDrawable(context, R.drawable.ic_search_24)
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val extras = FragmentNavigatorExtras(toolbar to "shared_element_container")
        toolbar.setOnClickListener {
            findNavController().navigate(R.id.fragment_search, null, null, extras)
        }
//        toolbar.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
    }

    companion object {
        const val TRANSITION_NAME = "search_view_transition"
        fun newInstance() = DictionaryFragment()
    }
}