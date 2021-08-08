package ru.nblackie.coredb.impl.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author tatarchukilya@gmail.com
 */
@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
internal abstract class DictionaryDataBase : RoomDatabase() {
    internal abstract fun dictionaryDao(): DictionaryDao
}