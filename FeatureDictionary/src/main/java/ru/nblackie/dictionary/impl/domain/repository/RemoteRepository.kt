package ru.nblackie.dictionary.impl.domain.repository

import ru.nblackie.dictionary.impl.data.model.Word

/**
 * @author Ilya Tatarchuk
 */
internal interface RemoteRepository {
    /**
     * @return количество слов в удаленном словаре
     */
    suspend fun count(lang: String): Int

    /**
     * Поиск слов (right join)
     */
    suspend fun search(input: String, lang: String, limit: Int): List<Word>
}