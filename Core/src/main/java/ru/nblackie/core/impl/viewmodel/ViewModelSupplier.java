package ru.nblackie.core.impl.viewmodel;

import androidx.lifecycle.SavedStateHandle;

/**
 * @author tatarchukilya@gmail.com
 */
public interface ViewModelSupplier<VM> {

    VM get(SavedStateHandle handle);
}
