package ru.nblackie.core.impl.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;
import androidx.savedstate.SavedStateRegistryOwner;

/**
 * @author tatarchukilya@gmail.com
 */
public interface ViewModelAssistedProvideFactory<VM extends ViewModel> {

    SaveStateViewModelProviderFactory<VM> create(SavedStateRegistryOwner owner, Bundle defaultArgs);
}
