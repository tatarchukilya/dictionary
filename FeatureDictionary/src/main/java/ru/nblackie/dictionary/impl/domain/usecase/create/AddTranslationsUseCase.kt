package ru.nblackie.dictionary.impl.domain.usecase.create

import ru.nblackie.dictionary.impl.domain.usecase.UseCase

/**
 * @author Ilya Tatarchuk
 */
internal interface AddTranslationsUseCase : UseCase {

    suspend fun run(word: String, transcription: String, translation: String)
}