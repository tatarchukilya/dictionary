package ru.nblackie.dictionary.di

import dagger.Module
import dagger.Provides
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.dictionary.api.di.DictionaryApi
import ru.nblackie.dictionary.impl.di.DictionaryDependencies
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.remote.api.RestApi
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
object FeatureAppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDictionaryDependencies(
        dataBaseApi: DataBaseApi,
        restApi: RestApi
    ): DictionaryDependencies =
        object : DictionaryDependencies {
            override fun dictionaryDao() = dataBaseApi.dao()
            override fun dictionaryApi() = restApi.dictionaryApi()

        }

    @JvmStatic
    @Provides
    fun provideDictionaryApi(dependencies: DictionaryDependencies): DictionaryApi {
        DictionaryFeatureHolder.init(dependencies)
        return DictionaryFeatureHolder.getApi()
    }
}