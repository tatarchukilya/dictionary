package ru.nblackie.dictionary.impl.di.internal

import ru.nblackie.core.impl.viewmodel.ViewModelProviderFactory
import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel

/**
 *  Предоставляет internal зависимости модуля
 *
 * @author tatarchukilya@gmail.com
 */
internal interface DictionaryFeatureInternalApi : DictionaryFeatureApi {

    fun sharedViewModel(): ViewModelProviderFactory<SharedViewModel>
}