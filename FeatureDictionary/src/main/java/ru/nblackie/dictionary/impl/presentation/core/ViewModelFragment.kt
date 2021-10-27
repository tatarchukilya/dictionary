package ru.nblackie.dictionary.impl.presentation.core

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import ru.nblackie.core.impl.viewmodel.ViewModelProviderFactory
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder

/**
 * @author tatarchukilya@gmail.com
 */
internal open class ViewModelFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    protected val viewModel: SharedViewModel by navGraphViewModels(R.id.graph_dictionary) {
        ViewModelProviderFactory({ SharedViewModel(DictionaryFeatureHolder.getInternalApi().useCases()) })
       // DictionaryFeatureHolder.getInternalApi().sharedViewModel()
    }
}