package ru.nblackie.dictionary.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.di.DataBaseDependencies
import ru.nblackie.coredb.impl.di.DataBaseFeatureHolder
import ru.nblackie.dictionary.DictionaryApplication
import ru.nblackie.remote.api.RestApi
import ru.nblackie.remote.impl.di.RestFeatureHolder
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object CoreAppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideContext(): Context {
        return DictionaryApplication.appContext
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBaseDependencies(context: Context): DataBaseDependencies =
        object : DataBaseDependencies {
            override fun context(): Context = context
        }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBaseApi(dependencies: DataBaseDependencies): DataBaseApi {
        DataBaseFeatureHolder.init(dependencies)
        return DataBaseFeatureHolder.getApi()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideRestApi(): RestApi {
        return RestFeatureHolder.getApi()
    }
}