package ru.nblackie.dictionary.impl.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.dictionary.impl.domain.converter.toItem
import ru.nblackie.dictionary.impl.domain.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryUseCaseImpl(private val repository: DictionaryRepository) :
    DictionaryUseCase {
    override suspend fun searchRemote(input: String): List<SearchWordItem> {
        return withContext(Dispatchers.IO) {
            repository.search(input).map { it.toItem() }
        }
    }

    override suspend fun addTranslation(word: String, transcription: String, translation: String) {
        repository.add(NewTranslation(word, transcription, translation))
    }

    override suspend fun searchDb(input: String, lang: String): List<SearchWordItem> {
        return withContext(Dispatchers.IO) {
            repository.searchDB(input, lang).map { it.toItem() }
        }
    }
}
