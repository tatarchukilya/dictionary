package ru.nblackie.remote.api

import ru.nblackie.coredi.FeatureApi
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
interface RestApi : FeatureApi {

    fun dictionaryApi() : RemoteDictionaryApi
}