package ru.nblackie.dictionary.impl.di.internal

import ru.nblackie.core.viewmodel.ViewModelAssistedProvideFactory
import ru.nblackie.core.viewmodel.ViewModelProviderFactory
import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModel
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModelNew
import ru.nblackie.dictionary.impl.presentation.search.SearchViewModel

/**
 *  Предоставляет internal зависимости модуля
 *
 * @author tatarchukilya@gmail.com
 */
internal interface DictionaryFeatureInternalApi : DictionaryFeatureApi {

    fun dictionaryViewModelProviderFactory(): ViewModelProviderFactory<DictionaryViewModel>

    fun searchViewModelProviderFactory(): ViewModelProviderFactory<SearchViewModel>

    fun dictionaryViewModelCreator(): ViewModelAssistedProvideFactory<DictionaryViewModelNew>
}