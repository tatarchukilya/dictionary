package ru.nblackie.dictionary.impl.presentation.dictionary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.core.ClearSearch
import ru.nblackie.dictionary.impl.presentation.core.ViewModelFragment

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryFragment : ViewModelFragment(R.layout.fragment_dictionary) {

    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar(view)
    }

    override fun onStart() {
        super.onStart()
        viewModel.handleAction(ClearSearch)
    }

    private fun setUpToolbar(view: View) {
        toolbar = view.findViewById<Toolbar>(R.id.toolbar).apply {
            navigationIcon =
                AppCompatResources.getDrawable(context, R.drawable.ic_search_24)
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setOnClickListener {
            toolbarAction()
        }
        toolbar.setNavigationOnClickListener {
            toolbarAction()
        }
    }

    private fun toolbarAction() {
        val extras =
            FragmentNavigatorExtras(toolbar to getString(R.string.dictionary_search_transition))
        findNavController().navigate(R.id.fragment_search, null, null, extras)
    }
}