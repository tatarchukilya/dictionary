package ru.nblackie.dictionary.impl.presentation.core

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Action
class SelectTranslation(val position: Int) : Action
class SelectWord(val position: Int) : Action
class ChangelLinks(val position: Int) : Action
class EditTranslation(input: String) : Action
object SavaChange : Action

sealed interface Event
object ShowPreview : Event