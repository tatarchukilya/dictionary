package ru.nblackie.settings.impl.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.coredi.PerFeature
import ru.nblackie.settings.R
import ru.nblackie.settings.presentation.SettingsContainerFragment

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object SettingsFeatureModule {

    @Provides
    @PerFeature
    fun provideContainerFragment(): ContainerFragment = SettingsContainerFragment()

    @Provides
    @PerFeature
    fun provideNavHostFragment(): NavHostFragment = NavHostFragment.create(R.navigation.settings)
}