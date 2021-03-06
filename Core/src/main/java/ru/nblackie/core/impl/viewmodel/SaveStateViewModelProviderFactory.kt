package ru.nblackie.core.impl.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

/**
 * @author tatarchukilya@gmail.com
 */
class SaveStateViewModelProviderFactory<VM : ViewModel?>(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle,
    private val supplier: ViewModelSupplier<VM>
) :
    AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val viewModel = supplier.get(handle)
        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}