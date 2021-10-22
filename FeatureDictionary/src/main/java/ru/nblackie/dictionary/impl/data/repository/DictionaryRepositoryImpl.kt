package ru.nblackie.dictionary.impl.data.repository

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.converter.toFullData
import ru.nblackie.dictionary.impl.data.converter.toSearchResult
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */

internal class DictionaryRepositoryImpl(
    private val api: RemoteDictionaryApi,
    private val dao: DictionaryDao
) : DictionaryRepository {

    private val cache = CacheImpl<String>()

    override suspend fun searchDB(input: String, lang: String): List<SearchResult> {
        return dao
            .searchRightJoin(input, lang)
            .groupBy { it.word }
            .map { it.toSearchResult() }
    }

    override suspend fun combineSearch(input: String, lang: String): List<SearchResult> {
        val remote = searchRemote(input, lang)
        val local = getTranslation(remote.map { it.word })
        return combineTranslation(remote, local)
    }

    override suspend fun add(data: NewTranslation) {
        dao.add(data.toFullData())
    }

    /**
     * Запрашивает варианты перевода с сервера по принципу RIGHT JOIN
     *
     * @param input введенное пользователем сллово
     * @param lang на каком языке искать
     */
    @Suppress("UNCHECKED_CAST")
    private suspend fun searchRemote(input: String, lang: String): RemoteResult {
        return cache.get(input, List::class.java) { str ->
            api.search(str, 20).result
        } as RemoteResult
    }

    /**
     * Получить все варианты переводов слов из [words]
     *
     * @param words список слов, для которых нужно получить перевод
     */
    private suspend fun getTranslation(words: List<String>): Translations {
        return dao
            .getTranslation(words)
            .groupBy { it.word }
    }

    /**
     * Совмещает данные с сервера с данными из БД
     */
    private fun combineTranslation(remote: RemoteResult, db: Translations): List<SearchResult> {
        return remote.map { it.toSearchResult(db[it.word]) }
    }
}