package ru.nblackie.remote.api

import ru.nblackie.coredi.BaseApi
import ru.nblackie.remote.impl.dictionary.DictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
interface RestApi : BaseApi {

    fun getDictionaryApi() : DictionaryApi
}