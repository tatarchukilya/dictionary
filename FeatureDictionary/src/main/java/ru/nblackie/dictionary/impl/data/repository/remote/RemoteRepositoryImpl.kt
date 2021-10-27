package ru.nblackie.dictionary.impl.data.repository.remote

import ru.nblackie.dictionary.impl.data.converter.toWord
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.dictionary.impl.domain.repository.RemoteRepository
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author Ilya Tatarchuk
 */
internal class RemoteRepositoryImpl(private val api: RemoteDictionaryApi) : RemoteRepository {

    override suspend fun count(lang: String): Int = api.count(lang).result

    override suspend fun search(input: String, lang: String, limit: Int): List<Word> {
        return api.search(input, lang, limit).result.map { it.toWord() }
    }
}