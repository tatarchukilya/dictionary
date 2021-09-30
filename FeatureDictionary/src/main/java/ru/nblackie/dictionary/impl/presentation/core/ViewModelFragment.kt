package ru.nblackie.dictionary.impl.presentation.viewmodel

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal open class ViewModelFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected val viewModel: SharedViewModel by navGraphViewModels(R.id.graph_dictionary) {
        DictionaryFeatureHolder.getInternalApi().sharedViewModel()
    }
}