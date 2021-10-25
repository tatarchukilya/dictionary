package ru.nblackie.dictionary.impl.presentation.core

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action

// Search
internal object ClearSearch : Action
internal class SearchInput(val input: String) : Action
internal class SelectWord(val position: Int) : Action
internal class SwitchSearch(val isLocal: Boolean) : Action

//Preview
internal object AddTranslation : Action
internal class MatchTranslation(val position: Int) : Action

//New
internal class NewTranslation(val input: String) : Action
internal object SaveNewTranslation : Action

internal sealed interface Event

//Search
internal object ShowPreview : Event

//Preview
internal object ShowNewWordView : Event

//Add
internal object StopSelf: Event