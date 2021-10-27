package ru.nblackie.dictionary.impl.domain.usecase.delete

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.dictionary.impl.domain.repository.DbRepository

/**
 * @author Ilya Tatarchuk
 */
internal class DeleteTranslationUseCaseImpl(private val repository: DbRepository) : DeleteTranslationUseCase {

    override suspend fun run(word: String, translation: String): Int {
        return withContext(Dispatchers.IO) {
            repository.deleteTranslation(word, translation)
        }
    }
}