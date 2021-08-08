package ru.nblackie.dictionary.impl.data.repository

import ru.nblackie.dictionary.impl.data.db.DictionaryDataBase
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapper
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryRepositoryImpl(
    private val apiMapper: DictionaryApiMapper,
    private val dataBase: DictionaryDataBase
) : DictionaryRepository {
}