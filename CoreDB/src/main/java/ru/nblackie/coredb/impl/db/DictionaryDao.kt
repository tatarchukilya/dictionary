package ru.nblackie.coredb.impl.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import ru.nblackie.coredb.impl.db.entity.Word

/**
 * @author tatarchukilya@gmail.com
 */
@Dao
interface DictionaryDao {

    @Query("SELECT * FROM word")
    suspend fun getDictionary(): List<Word>

    @Insert(onConflict = REPLACE)
    suspend fun addWord(wordEntity: Word?)

    @Query("DELETE FROM word WHERE word = :word")
    suspend fun deleteWord(word: String)
}