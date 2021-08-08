package ru.nblackie.dictionary.di

import ru.nblackie.dictionary.api.di.DictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
interface FeatureProviderApi {

    fun getDictionaryApi(): DictionaryApi

}