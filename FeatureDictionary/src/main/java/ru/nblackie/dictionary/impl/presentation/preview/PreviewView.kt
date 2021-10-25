package ru.nblackie.dictionary.impl.presentation.preview

import ru.nblackie.dictionary.impl.presentation.core.MatchTranslation
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.PreviewState

/**
 * @author Ilya Tatarchuk
 */
internal interface PreviewView {

    //State
    fun setState(state: PreviewState)

    //Action
    fun matchTranslation(action: MatchTranslation)

    //Event
    fun showAddView()
}