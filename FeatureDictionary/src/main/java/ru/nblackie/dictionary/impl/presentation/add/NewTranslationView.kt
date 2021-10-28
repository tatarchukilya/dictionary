package ru.nblackie.dictionary.impl.presentation.add

/**
 * @author Ilya Tatarchuk
 */
internal interface NewTranslationView {

    //Action
    fun setNewTranslation(input: String)

    fun saveNewTranslation()

    //Event
    fun stopSelf()
}