package ru.nblackie.core.impl.viewmodel

import androidx.core.util.Consumer
import androidx.core.util.Supplier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import javax.inject.Inject

/**
 * @author tatarchukilya@gmail.com
 */
class ViewModelProviderFactory<VM> @Inject constructor(
    private val supplier: Supplier<VM>,
    private val onCreatedHook: Consumer<VM>? = null
) : NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: VM = supplier.get().apply { onCreatedHook?.accept(this) }
        @Suppress("UNCHECKED_CAST")
        return viewModel as T
    }
}