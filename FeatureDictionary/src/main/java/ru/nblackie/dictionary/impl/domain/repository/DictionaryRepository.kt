package ru.nblackie.dictionary.impl.domain.repository

import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryRepository {

    suspend fun search(input: String): List<Word>
}