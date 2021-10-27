package ru.nblackie.dictionary.impl.domain.usecase.create

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nblackie.dictionary.impl.data.model.NewTranslation
import ru.nblackie.dictionary.impl.domain.repository.DbRepository

/**
 * @author Ilya Tatarchuk
 */
internal class AddTranslationsUseCaseImpl(private val repository: DbRepository) : AddTranslationsUseCase {

    override suspend fun run(word: String, transcription: String, translation: String) {
        withContext(Dispatchers.IO) {
            repository.add(NewTranslation(word, transcription, translation))
        }
    }
}