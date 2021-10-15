package ru.nblackie.dictionary.impl.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.converter.toDataSaveEnRu
import ru.nblackie.dictionary.impl.data.converter.toListResult
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryRepositoryImpl(
    private val api: RemoteDictionaryApi,
    private val dao: DictionaryDao
) : DictionaryRepository {

    private val cache = CacheImpl<String>()

    override suspend fun search(input: String): List<Word> =
        withContext(Dispatchers.IO) {
            cache.get(input, List::class.java) { str ->
                api.search(str, 20).result
            } as List<Word>
        }

    override suspend fun add(data: NewTranslation) {
        withContext(Dispatchers.IO) {
            dao.add(data.toDataSaveEnRu())
        }
    }

    override suspend fun searchDB(input: String, lang: String): List<SearchResult> {
        return dao.searchRightJoin("$input%", lang).toListResult()
    }
}