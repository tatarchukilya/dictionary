package ru.nblackie.dictionary.impl.domain.interactor

import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryUseCase {

    suspend fun search(input: String): List<Word>
}