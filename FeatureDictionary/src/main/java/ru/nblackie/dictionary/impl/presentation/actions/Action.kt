package ru.nblackie.dictionary.impl.presentation.actions

import android.view.View

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action {
    class Click(val position: Int, val view: View) : Action
}

