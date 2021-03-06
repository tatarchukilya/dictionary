package ru.nblackie.coredb.impl.di.inner

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredb.impl.db.DictionaryDataBase
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object DataBaseModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): DictionaryDataBase = DictionaryDataBase.getDatabase(context)

    @Provides
    @Singleton
    fun provideDao(dataBase: DictionaryDataBase): DictionaryDao = dataBase.dictionaryDao()
}