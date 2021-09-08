package ru.nblackie.exercise.impl.presentation

import androidx.fragment.app.Fragment
import ru.nblackie.core.fragment.ContainerFragment

/**
 * @author tatarchukilya@gmail.com
 */
class ExerciseContainerFragment : ContainerFragment() {

    override fun rootFragment(): Fragment = ExerciseRootFragment()
}