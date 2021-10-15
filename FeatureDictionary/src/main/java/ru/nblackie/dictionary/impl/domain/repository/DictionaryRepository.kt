package ru.nblackie.dictionary.impl.domain.repository

import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryRepository {

    suspend fun search(input: String): List<Word>

    suspend fun add(data: NewTranslation)

    suspend fun searchDB(input: String, lang: String): List<SearchResult>
}