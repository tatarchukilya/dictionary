package ru.nblackie.dictionary.impl.domain.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.remote.impl.dictionary.model.Word

/**
 * @author tatarchukilya@gmail.com
 */
class DictionaryUseCaseImpl(private val repository: DictionaryRepository) :
    DictionaryUseCase {
    override suspend fun search(input: String): List<Word> {
        return withContext(Dispatchers.IO) {
            repository.search(input)
        }
    }

}
