package ru.nblackie.dictionary.impl.domain.usecase.delete

/**
 * @author Ilya Tatarchuk
 */
internal interface DeleteTranslationUseCase {

    suspend fun run(word: String, translation: String): Int
}