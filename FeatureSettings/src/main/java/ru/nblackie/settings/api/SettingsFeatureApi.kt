package ru.nblackie.settings.api

import androidx.navigation.fragment.NavHostFragment
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.coredi.FeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
interface SettingsFeatureApi : FeatureApi {

    fun navHostFragment(): NavHostFragment

    fun containerFragment(): ContainerFragment
}