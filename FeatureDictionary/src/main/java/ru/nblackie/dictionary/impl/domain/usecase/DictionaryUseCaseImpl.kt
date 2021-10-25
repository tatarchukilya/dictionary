package ru.nblackie.dictionary.impl.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.data.Lang
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.converter.toItem
import ru.nblackie.dictionary.impl.domain.converter.toSearchSpannableItem
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.model.SearchItem
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository

/**
 * @author tatarchukilya@gmail.com
 */
internal class DictionaryUseCaseImpl(
    private val repository: DictionaryRepository,
    private val resourceManager: ResourceManager
) : DictionaryUseCase {
    override suspend fun combineSearch(input: String, lang: Lang, limit: Int): List<SearchItem> {
        return withContext(Dispatchers.IO) {
            repository.combineSearch(input, lang.code, limit)
                .map {
                    it.toSearchSpannableItem(resourceManager)
                }
        }
    }

    override suspend fun addTranslation(word: String, transcription: String, translation: String) {
        withContext(Dispatchers.IO) {
            repository.add(NewTranslation(word, transcription, translation))
        }
    }

    override suspend fun searchDb(input: String, lang: Lang): List<SearchItem> {
        return withContext(Dispatchers.IO) {
            repository.searchDB(input, lang.code).map { it.toItem() }
        }
    }

    override suspend fun getSingleWord(word: String, lang: Lang): SearchResult? {
        return withContext(Dispatchers.IO) {
            repository.getSingleWord(word, lang.code)
        }
    }

    override suspend fun getTranslation(word: String): List<String> {
        return withContext(Dispatchers.IO) {
            repository.getTranslation(word)
        }
    }

    override suspend fun deleteTranslation(word: String, translation: String): Int {
        return withContext(Dispatchers.IO) {
            repository.deleteTranslation(word, translation)
        }
    }

    override suspend fun count(lang: Lang): Int {
        return withContext(Dispatchers.IO) {
            repository.count(lang.code)
        }
    }
}
