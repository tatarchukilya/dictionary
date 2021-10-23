package ru.nblackie.coredb.impl.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import ru.nblackie.coredb.impl.db.data.WordFullData
import ru.nblackie.coredb.impl.db.entity.TranslationEntity
import ru.nblackie.coredb.impl.db.entity.WordEntity
import ru.nblackie.coredb.impl.db.data.FullSearchRow
import ru.nblackie.coredb.impl.db.data.TranslationSearchRow

/**
 * @author tatarchukilya@gmail.com
 */
@Dao
interface DictionaryDao {

    @Query("SELECT w2.id as id, w2.word as word, w2.transcription as transcription, w1.word as translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE word LIKE :input AND lang = :lang) as t)")
    suspend fun search(input: String, lang: String): List<FullSearchRow>

    @Query("SELECT id FROM word WHERE word = :word")
    suspend fun getWordId(word: String): Long?

    @Query("SELECT w2.id as id, w2.word as word, w2.transcription as transcription, w1.word as translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE word = :word AND lang = :lang) as t)")
    suspend fun getSingleWord(word: String, lang: String): List<FullSearchRow>

    @Query("SELECT w2.word as word, w1.word as translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE word IN (:words)) as t)")
    suspend fun getTranslations(words: List<String>): List<TranslationSearchRow>

    @Query("SELECT w2.word as word, w1.word as translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE word = :word) as t)")
    suspend fun getTranslation(word: String): List<TranslationSearchRow>

    @Query("SELECT w2.id, w2.word, w2.transcription, w1.word translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE  lang = :lang ORDER BY word  LIMIT :offset, :limit) as t) ORDER BY w2.word")
    suspend fun getPagingDictionary(lang: String, limit: Int, offset: Int): List<FullSearchRow>

    @Query("SELECT w2.id, w2.word, w2.transcription, w1.word translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE  lang = :lang) as t) ORDER BY w2.word")
    suspend fun getDictionary(lang: String): List<FullSearchRow>

    @Query("DELETE FROM translation WHERE (word = :word OR word = :translation) AND (translation = :translation OR translation = :word)")
    suspend fun deleteTranslation(word: Long, translation: Long): Int

    @Insert(onConflict = IGNORE)
    suspend fun addWord(wordEntity: WordEntity): Long

    @Insert(onConflict = IGNORE)
    suspend fun addTranslation(translation: TranslationEntity): Long

    @Query("DELETE FROM word WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Transaction
    suspend fun add(data: WordFullData) {
        data.run {
            val wordId = getWordId(word.data) ?: addWord(
                WordEntity(
                    word = word.data,
                    transcription = transcription,
                    lang = word.lang
                )
            )
            translation.forEach {
                val translationId =
                    getWordId(it.data) ?: addWord(WordEntity(word = it.data, lang = it.lang))
                addTranslation(TranslationEntity(word = wordId.toInt(), translation = translationId.toInt()))
                addTranslation(TranslationEntity(word = translationId.toInt(), translation = wordId.toInt()))
            }
        }
    }

    @Transaction
    suspend fun deleteTranslationByWord(word: String, translation: String): Int {
        val wordId = getWordId(word)
        val translationId = getWordId(translation)
        return when (wordId != null && translationId != null) {
            true -> deleteTranslation(wordId, translationId)
            false -> -1
        }
    }

    @Transaction
    suspend fun searchRightJoin(input: String, lang: String): List<FullSearchRow> {
        return search("$input%", lang)
    }
}
