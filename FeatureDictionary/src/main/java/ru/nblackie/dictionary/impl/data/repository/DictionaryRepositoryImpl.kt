package ru.nblackie.dictionary.impl.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapper
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryRepositoryImpl(
    private val apiMapper: DictionaryApiMapper,
    private val dao: DictionaryDao
) : DictionaryRepository {

    private val cache = CacheImpl<String>()

    override suspend fun search(input: String): List<Word> =
        withContext(Dispatchers.IO) {
            cache.get(input, List::class.java) { str ->
                apiMapper.search(str, 20).result
            } as List<Word>
        }
}