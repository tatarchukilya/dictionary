package ru.nblackie.dictionary.di

import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.exercise.api.ExerciseFeatureApi
import ru.nblackie.settings.api.SettingsFeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
internal interface FeatureApiProvider {

    fun getDictionaryApi(): DictionaryFeatureApi

    fun exerciseFeatureApi(): ExerciseFeatureApi

    fun settingsFeatureApi() : SettingsFeatureApi

}