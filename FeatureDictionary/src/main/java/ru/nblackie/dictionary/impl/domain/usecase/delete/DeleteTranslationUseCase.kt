package ru.nblackie.dictionary.impl.domain.usecase.delete

import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface DeleteTranslationUseCase : UseCase {

    suspend fun run(word: String, translation: String): Int
}