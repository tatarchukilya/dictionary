package ru.nblackie.coredb.impl.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.nblackie.coredb.impl.db.entity.TranslationEntity
import ru.nblackie.coredb.impl.db.entity.WordEntity

/**
 * @author tatarchukilya@gmail.com
 */
@Database(entities = [WordEntity::class, TranslationEntity::class], version = 1, exportSchema = false)
internal abstract class DictionaryDataBase : RoomDatabase() {

    internal abstract fun dictionaryDao(): DictionaryDao

    companion object {
        @Volatile
        private var INSTANCE: DictionaryDataBase? = null

        fun getDatabase(context: Context): DictionaryDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    DictionaryDataBase::class.java,
                    "dictionary_database"
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
