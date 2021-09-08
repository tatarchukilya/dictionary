package ru.nblackie.dictionary.api.di

import androidx.navigation.fragment.NavHostFragment
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.coredi.FeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryFeatureApi : FeatureApi {

    fun containerFragment(): ContainerFragment

    fun navHostFragment() : NavHostFragment
}