package ru.nblackie.dictionary.impl.data.repository

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredb.impl.db.data.TranslationSearchRow
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.converter.getWords
import ru.nblackie.dictionary.impl.data.converter.toFullData
import ru.nblackie.dictionary.impl.data.converter.toListResult
import ru.nblackie.dictionary.impl.data.converter.toSearchResult
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */

class DictionaryRepositoryImpl(
    private val api: RemoteDictionaryApi,
    private val dao: DictionaryDao
) : DictionaryRepository {

    private val cache = CacheImpl<String>()

    override suspend fun searchDB(input: String, lang: String): List<SearchResult> {
        return dao.searchRightJoin("$input%", lang).toListResult()
    }

    override suspend fun combineSearch(input: String, lang: String): List<SearchResult> {
        val remote = searchRemote(input, lang)
        val local = getTranslation(remote.getWords())
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
    private suspend fun getTranslation(words: List<String>): Map<String, List<TranslationSearchRow>> {
        return dao.getTranslation(words).groupBy {
            it.word
        }
    }

    /**
     * Совмещает данные с сервера с данными из БД
     */
    private fun combineTranslation(remote: RemoteResult, db: Translations): List<SearchResult> {
        val result = mutableListOf<SearchResult>()
        remote.forEach {
            val localTranslation = mutableListOf<String>()
            db[it.word]?.forEach { translation ->
                localTranslation.add(translation.translation)
            }
            result.add(it.toSearchResult(localTranslation))
        }
        return result
    }
}