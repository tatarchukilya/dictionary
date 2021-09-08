package ru.nblackie.dictionary.di

import dagger.Module
import dagger.Provides
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.dictionary.impl.di.DictionaryDependencies
import ru.nblackie.dictionary.impl.di.DictionaryFeatureHolder
import ru.nblackie.exercise.api.ExerciseFeatureApi
import ru.nblackie.exercise.impl.di.ExerciseFeatureDependency
import ru.nblackie.exercise.impl.di.ExerciseFeatureHolder
import ru.nblackie.remote.api.RestApi
import ru.nblackie.settings.api.SettingsFeatureApi
import ru.nblackie.settings.impl.SettingsFeatureHolder
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object FeatureAppModule {

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

    @Provides
    fun provideDictionaryApi(dependencies: DictionaryDependencies): DictionaryFeatureApi {
        DictionaryFeatureHolder.init(dependencies)
        return DictionaryFeatureHolder.getApi()
    }

    @Singleton
    @Provides
    fun provideExerciseFeatureDependencies(
        dataBaseApi: DataBaseApi
    ): ExerciseFeatureDependency =
        object : ExerciseFeatureDependency {
            override fun dao(): DictionaryDao = dataBaseApi.dao()
        }

    @Provides
    fun provideExerciseFeatureApi(dependencies: ExerciseFeatureDependency): ExerciseFeatureApi {
        ExerciseFeatureHolder.init(dependencies)
        return ExerciseFeatureHolder.getApi()
    }

    @Provides
    fun provideSettingsFeatureApi(): SettingsFeatureApi = SettingsFeatureHolder.getApi()
}