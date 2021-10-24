package ru.nblackie.dictionary.impl.presentation.core

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action
class SelectTranslation(val position: Int) : Action
class SelectWord(val position: Int) : Action
class MatchTranslation(val position: Int) : Action
class AddNewTranslation(val translation: String) : Action

sealed interface Event
object ShowPreview : Event
object SpotPreview: Event