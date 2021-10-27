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
     * Возвращает данные о слове (только точное совпадение c [word])
     *
     * @param word слово, данные для которого необходимо получить
     * @param lang язык поиска
     */
    suspend fun getSingleWord(word: String, lang: String): Word?

    /**
     * Получить все варианты перевода из БД для указанного слова (только точное совпадение c [word])
     *
     * @param word слово, данные для которого необходимо получить
     */
    suspend fun getTranslation(word: String): List<String>

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
}