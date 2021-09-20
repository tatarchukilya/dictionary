package ru.nblackie.dictionary.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.nblackie.core.api.CoreApi
import ru.nblackie.core.impl.di.CoreDependency
import ru.nblackie.core.impl.di.CoreFeatureHolder
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.di.DataBaseDependency
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

    @Singleton
    @Provides
    fun provideContext(): Context {
        return DictionaryApplication.appContext
    }

    @Singleton
    @Provides
    fun provideDataBaseDependencies(context: Context): DataBaseDependency =
        object : DataBaseDependency {
            override fun context(): Context = context
        }

    @Singleton
    @Provides
    fun provideDataBaseApi(dependencies: DataBaseDependency): DataBaseApi {
        DataBaseFeatureHolder.init(dependencies)
        return DataBaseFeatureHolder.getApi()
    }

    @Singleton
    @Provides
    fun provideCoreDependency(context: Context): CoreDependency =
        object : CoreDependency {
            override fun context(): Context = context
        }

    @Singleton
    @Provides
    fun provideCoreApi(dependency: CoreDependency): CoreApi {
        CoreFeatureHolder.init(dependency)
        return CoreFeatureHolder.getApi()
    }

    @Provides
    @Singleton
    fun provideRestApi(): RestApi {
        return RestFeatureHolder.getApi()
    }
}