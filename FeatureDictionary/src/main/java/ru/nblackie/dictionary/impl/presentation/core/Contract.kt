package ru.nblackie.dictionary.impl.presentation.core

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action {
    //Dictionary
    object ShowSearch: Action
    class DictionarySelect(val position: Int) : Action

    // Search
    object ClearSearch : Action
    class SearchInput(val input: String) : Action
    class SearchSelect(val position: Int) : Action
    class SwitchSearch(val isLocal: Boolean) : Action

    //Detail
    object AddTranslation : Action
    class MatchTranslation(val position: Int) : Action
    class SelectWord(val position: Int, val source: DetailSource) : Action

    //New
    class NewTranslation(val input: String) : Action
    object SaveNewTranslation : Action

}

internal sealed interface Event {
    //Dictionary
    object ShowSearch: Event

    //Search
    object ShowDetail : Event

    //Preview
    object ShowAddView : Event

    //Add
    object StopSelf : Event
}

internal enum class DetailSource {
    DICTIONARY, SEARCH
}