package ru.nblackie.dictionary.impl.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.data.Lang
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
    override suspend fun combineSearch(input: String, lang: Lang): List<SearchItem> {
        return withContext(Dispatchers.IO) {
            repository.combineSearch(input, lang.code)
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
}
