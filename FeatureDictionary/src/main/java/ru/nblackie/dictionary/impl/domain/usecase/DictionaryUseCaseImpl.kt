package ru.nblackie.dictionary.impl.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.domain.converter.toItem
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryUseCaseImpl(private val repository: DictionaryRepository) : DictionaryUseCase {
    override suspend fun combineSearch(input: String, lang: Lang): List<SearchWordItem> {
        return withContext(Dispatchers.IO) {
            repository.combineSearch(input, lang.code).map { it.toItem() }
        }
    }

    override suspend fun addTranslation(word: String, transcription: String, translation: String) {
        withContext(Dispatchers.IO) {
            repository.add(NewTranslation(word, transcription, translation))
        }
    }

    override suspend fun searchDb(input: String, lang: Lang): List<SearchWordItem> {
        return withContext(Dispatchers.IO) {
            repository.searchDB(input, lang.code).map { it.toItem() }
        }
    }
}
