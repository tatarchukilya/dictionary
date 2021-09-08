package ru.nblackie.dictionary.impl.presentation.dictionary

import androidx.lifecycle.SavedStateHandle

/**
 * @author tatarchukilya@gmail.com
 */
interface VMSupplier<T> {

    fun get(handle: SavedStateHandle): T
}