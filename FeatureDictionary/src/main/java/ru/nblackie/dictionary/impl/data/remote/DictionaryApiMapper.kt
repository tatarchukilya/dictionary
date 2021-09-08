package ru.nblackie.dictionary.impl.data.remote

import ru.nblackie.remote.impl.dictionary.model.SearchResponse

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryApiMapper {

    suspend fun search(input: String, limit: Int): SearchResponse
}