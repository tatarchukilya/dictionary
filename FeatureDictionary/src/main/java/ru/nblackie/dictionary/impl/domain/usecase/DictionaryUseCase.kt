package ru.nblackie.dictionary.impl.domain.usecase

import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryUseCase {

    suspend fun searchRemote(input: String): List<SearchWordItem>

    suspend fun addTranslation(word: String, transcription: String, translation: String)

    suspend fun searchDb(input: String, lang: String = "en"): List<SearchWordItem>
}