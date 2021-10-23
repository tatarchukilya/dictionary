package ru.nblackie.dictionary.impl.domain.repository

import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.NewTranslation

/**
 * @author tatarchukilya@gmail.com
 */
internal interface DictionaryRepository {

    /**
     * Запрашивает варианты перевода с сервера и подтягивает к ним варианты из БД
     * @param input введенное пользователем сллово
     * @param lang язык словаря
     */
    suspend fun combineSearch(input: String, lang: String): List<SearchResult>

    /**
     * Добавить слово с переводом в БД
     * @param data word, transcription, translation
     */
    suspend fun add(data: NewTranslation)

    /**
     * Поиск по БД
     * @param input введенное слово
     * @param lang язык словаря
     */
    suspend fun searchDB(input: String, lang: String): List<SearchResult>

    /**
     * Возвращает данные о слове (только точное совпадение)
     *
     * @param word слово, данные для которого необходимо получить
     */
    suspend fun getSingleWord(word: String, lang: String): SearchResult?

    /**
     * Получить все варианты перевода из БД для указанного слова
     */
    suspend fun getTranslation(word: String): List<String>

    /**
     * Удалить перевод для указанного влова
     */
    suspend fun deleteTranslation(word: String, translation: String) : Int
}