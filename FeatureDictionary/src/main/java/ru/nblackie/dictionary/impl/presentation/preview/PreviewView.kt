package ru.nblackie.dictionary.impl.presentation.preview

import ru.nblackie.dictionary.impl.presentation.core.Action

/**
 * @author Ilya Tatarchuk
 */
internal interface PreviewView {

    fun selectTranslation(action: Action)
}