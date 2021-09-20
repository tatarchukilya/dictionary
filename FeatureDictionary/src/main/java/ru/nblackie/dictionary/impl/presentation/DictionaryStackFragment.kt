package ru.nblackie.dictionary.impl.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.nblackie.core.impl.fragment.ContainerFragment
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryFragment
import ru.nblackie.dictionary.impl.presentation.search.SearchFragment

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryStackFragment : ContainerFragment() {

    override fun rootFragment(): Fragment = DictionaryFragment()

    companion object {
        fun newInstance() = DictionaryStackFragment()
    }

    override fun reselect() {
        parentFragmentManager.commit {
            attach(this@DictionaryStackFragment)
        }

        with(childFragmentManager) {
            if (findFragmentByTag("SearchTag") == null) {
                commit {
                    add(R.id.child_fragment_container, SearchFragment(), "SearchTag")
                    addToBackStack(null)
                }
            }
//            findFragmentByTag("SearchTag")?.let {
//                //TODO popBackStack before SearchFragment
//            } ?: commit {
//                add(R.id.child_fragment_container, SearchFragment.newInstance(), "SearchTag")
//                addToBackStack(null)
//            }
        }
    }
}