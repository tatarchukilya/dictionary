package ru.nblackie.dictionary.impl.domain.usecase

import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryUseCase {

    suspend fun search(input: String): List<SearchWordItem>
}