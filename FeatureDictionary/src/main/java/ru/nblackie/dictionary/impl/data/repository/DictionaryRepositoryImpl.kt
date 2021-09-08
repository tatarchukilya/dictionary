package ru.nblackie.dictionary.impl.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.coredb.impl.db.DictionaryDao
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

    override suspend fun search(input: String): List<Word> =
        withContext(Dispatchers.IO) {
            apiMapper.search(input, 20).result
        }
}