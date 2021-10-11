package ru.nblackie.dictionary.impl.presentation.actions

/**
 * @author Ilya Tatarchuk
 */
internal sealed interface Event {
    class Click(val position: Int) : Event
}

