package ru.nblackie.dictionary.impl.data.repository.db

import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.dictionary.impl.data.converter.toFullData
import ru.nblackie.dictionary.impl.data.model.NewTranslation
import ru.nblackie.dictionary.impl.data.model.Word
import ru.nblackie.dictionary.impl.domain.repository.DbRepository

/**
 * @author Ilya Tatarchuk
 */
internal class DbRepositoryImpl(private val dao: DictionaryDao) : DbRepository {

    override suspend fun search(input: String, lang: String): List<Word> {
        return dao.searchRightJoin(input, lang)
            .groupBy { it.word }
            .map {
                Word(
                    it.key,
                    it.value[0].transcription,
                    it.value.map { data -> data.translation }
                )
            }
    }

    override suspend fun getSingleWord(word: String, lang: String): Word? {
        return dao
            .getSingleWord(word, lang)
            .groupBy { it.word }
            .map {
                Word(
                    it.key,
                    it.value[0].transcription,
                    it.value.map { data -> data.translation }
                )
            }
            .firstOrNull()
    }

    override suspend fun getTranslation(word: String): List<String> {
        return dao.getTranslation(word).map { it.translation }
    }

    override suspend fun deleteTranslation(word: String, translation: String): Int {
        return dao.deleteTranslationByWord(word, translation)
    }

    override suspend fun add(data: NewTranslation) {
        dao.add(data.toFullData())
    }
}