package ru.nblackie.dictionary.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nblackie.dictionary.DictionaryApplication
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return DictionaryApplication.appContext
    }
}