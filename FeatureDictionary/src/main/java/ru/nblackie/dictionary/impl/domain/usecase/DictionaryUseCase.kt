package ru.nblackie.dictionary.impl.domain.usecase

import ru.nblackie.core.impl.data.Lang
import ru.nblackie.core.impl.data.Lang.*
import ru.nblackie.dictionary.impl.domain.model.SearchWordItem

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryUseCase {

    /**
     * Запрашивает варианты перевода с сервера и подтягивает к ним варианты из БД
     * @param input введенное пользователем сллово
     * @param lang на каком языке искать
     */
    suspend fun combineSearch(input: String, lang: Lang = EN): List<SearchWordItem>

    /**
     * Добавить слово с переводом в БД
     */
    suspend fun addTranslation(word: String, transcription: String, translation: String)

    /**
     * Поиск по БД
     */
    suspend fun searchDb(input: String, lang: Lang = EN): List<SearchWordItem>
}