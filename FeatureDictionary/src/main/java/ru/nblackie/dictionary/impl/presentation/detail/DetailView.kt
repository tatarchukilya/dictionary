package ru.nblackie.dictionary.impl.presentation.detail

import ru.nblackie.dictionary.impl.presentation.core.Action.MatchTranslation
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel.DetailState

/**
 * @author Ilya Tatarchuk
 */
internal interface DetailView {

    //State
    fun setState(state: DetailState)

    //Action
    fun matchTranslation(action: MatchTranslation)

    fun sendNewWordAction()

    //Event
    fun showNewTranslationView()
}