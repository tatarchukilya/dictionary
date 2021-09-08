package ru.nblackie.dictionary.impl.data.remote

import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi
import ru.nblackie.remote.impl.dictionary.model.SearchResponse

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryApiMapperImpl(private val dictionaryApi: RemoteDictionaryApi) :
    DictionaryApiMapper {

    override suspend fun search(input: String, limit: Int): SearchResponse =
        dictionaryApi.search(input, limit)
}