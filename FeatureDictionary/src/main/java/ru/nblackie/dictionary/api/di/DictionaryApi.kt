package ru.nblackie.dictionary.api.di

import androidx.navigation.fragment.NavHostFragment
import ru.nblackie.coredi.FeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
interface DictionaryApi : FeatureApi {

    fun navHostFragment(): NavHostFragment
}