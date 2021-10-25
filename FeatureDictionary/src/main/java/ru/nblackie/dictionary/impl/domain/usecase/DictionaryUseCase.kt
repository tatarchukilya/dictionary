package ru.nblackie.dictionary.impl.domain.usecase

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.core.impl.data.Lang.EN
import ru.nblackie.dictionary.impl.data.model.SearchResult
import ru.nblackie.dictionary.impl.domain.model.SearchItem

/**
 * @author tatarchukilya@gmail.com
 */
internal interface DictionaryUseCase {

    /**
     * Запрашивает варианты перевода с сервера и подтягивает к ним варианты из БД
     * @param input введенное пользователем сллово
     * @param lang на каком языке искать
     */
    suspend fun combineSearch(input: String, lang: Lang = EN, limit: Int = 20): List<SearchItem>

    /**
     * Добавить слово с переводом в БД
     */
    suspend fun addTranslation(word: String, transcription: String, translation: String)

    /**
     * Поиск по БД
     */
    suspend fun searchDb(input: String, lang: Lang = EN): List<SearchItem>

    suspend fun getSingleWord(word: String, lang: Lang = EN) : SearchResult?

    suspend fun getTranslation(word: String): List<String>

    suspend fun deleteTranslation(word: String, translation: String): Int

    suspend fun count(lang: Lang = EN): Int
}