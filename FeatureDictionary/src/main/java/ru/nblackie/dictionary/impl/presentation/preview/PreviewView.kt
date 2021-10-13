package ru.nblackie.dictionary.impl.presentation.preview

import ru.nblackie.dictionary.impl.presentation.actions.Event

/**
 * @author Ilya Tatarchuk
 */
internal interface PreviewView {

    fun selectTranslation(event: Event)

    fun addToPersonal()
}