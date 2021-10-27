package ru.nblackie.dictionary.impl.domain.usecase.create

/**
 * @author Ilya Tatarchuk
 */
internal interface AddTranslationsUseCase {

    suspend fun run(word: String, transcription: String, translation: String)
}