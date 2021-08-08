package ru.nblackie.coredb.impl.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

/**
 * @author tatarchukilya@gmail.com
 */
@Dao
interface DictionaryDao {

    @Query("SELECT * FROM dictionary")
    suspend fun getDictionary(): List<WordEntity>

    @Insert(onConflict = REPLACE)
    suspend fun addWord(wordEntity: WordEntity?)

    @Query("DELETE FROM dictionary WHERE word = :word")
    suspend fun deleteWord(word: String)
}