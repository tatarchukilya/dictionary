package ru.nblackie.core.impl.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.nblackie.core.R

/**
 * @author tatarchukilya@gmail.com
 */
abstract class ContainerFragment : Fragment() {

    abstract fun rootFragment(): Fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                add(R.id.child_fragment_container, rootFragment())
            }
        }
    }

    fun navigateUp(): Boolean {
        with(childFragmentManager) {
            return if (fragments.size > 1) {
                popBackStack()
                true
            } else {
                false
            }
        }
    }

    open fun reselect() {

    }
}