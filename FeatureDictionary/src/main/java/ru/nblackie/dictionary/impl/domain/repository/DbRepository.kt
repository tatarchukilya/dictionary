package ru.nblackie.dictionary.impl.domain.repository

import ru.nblackie.dictionary.impl.data.model.NewTranslation
import ru.nblackie.dictionary.impl.data.model.Word

/**
 * @author Ilya Tatarchuk
 */
internal interface DbRepository {

    /**
     * Поиск по БД
     * @param input введенное слово
     * @param lang язык поиска
     */
    suspend fun search(input: String, lang: String): List<Word>

    /**
     * Удалить перевод для указанного влова
     */
    suspend fun deleteTranslation(word: String, translation: String): Int

    /**
     * Добавить слово с переводом в БД
     *
     * @param data word, transcription, translation
     */
    suspend fun add(data: NewTranslation)

    suspend fun getDictionary(lang: String): List<Word>
}