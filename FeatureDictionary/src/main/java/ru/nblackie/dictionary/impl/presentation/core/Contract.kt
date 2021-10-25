package ru.nblackie.dictionary.impl.presentation.core

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action

// Search
object ClearSearch : Action
class SearchInput(val input: String) : Action
class SelectWord(val position: Int) : Action
class SwitchSearch(val isLocal: Boolean) : Action

//Preview
object AddTranslation : Action
class MatchTranslation(val position: Int) : Action

sealed interface Event
object ShowPreview : Event
object ShowAddView : Event