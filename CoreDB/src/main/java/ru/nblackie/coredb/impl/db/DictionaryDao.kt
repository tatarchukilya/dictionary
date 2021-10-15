package ru.nblackie.coredb.impl.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import ru.nblackie.coredb.impl.db.data.WordData
import ru.nblackie.coredb.impl.db.entity.TranslationEntity
import ru.nblackie.coredb.impl.db.entity.WordEntity
import ru.nblackie.coredb.impl.db.data.SearchRow

/**
 * @author tatarchukilya@gmail.com
 */
@Dao
interface DictionaryDao {

    @Query("SELECT w2.id as id, w2.word as word, w2.transcription as transcription, w1.word as translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE word LIKE :input AND lang = :lang) as t)")
    suspend fun search(input: String, lang: String): List<SearchRow>

    @Query("SELECT id FROM word WHERE word = :word")
    suspend fun getWordId(word: String): Long?

    @Query("SELECT w2.id, w2.word, w2.transcription, w1.word translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE  lang = :lang ORDER BY word  LIMIT :offset, :limit) as t) ORDER BY w2.word")
    suspend fun getPagingDictionary(lang: String, limit: Int, offset: Int): List<SearchRow>

    @Query("SELECT w2.id, w2.word, w2.transcription, w1.word translation FROM translation t1 INNER JOIN word w1 ON t1.translation = w1.id INNER JOIN word w2 ON t1.word = w2.id WHERE t1.word IN (SELECT * FROM (SELECT id FROM word WHERE  lang = :lang) as t) ORDER BY w2.word")
    suspend fun getDictionary(lang: String): List<SearchRow>

    @Insert(onConflict = IGNORE)
    suspend fun addWord(wordEntity: WordEntity): Long

    @Insert(onConflict = IGNORE)
    suspend fun addTranslation(translation: TranslationEntity): Long

    @Query("DELETE FROM word WHERE word = :word")
    suspend fun deleteWord(word: String)

    @Transaction
    suspend fun add(data: WordData) {
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
    suspend fun searchRightJoin(input: String, lang: String): List<SearchRow> {
        return search("$input%", lang)
    }
}
