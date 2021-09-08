package ru.nblackie.settings.impl.internal

import dagger.Component
import ru.nblackie.coredi.PerFeature

/**
 * @author tatarchukilya@gmail.com
 */
@PerFeature
@Component(modules = [SettingsFeatureModule::class])
internal abstract class SettingsFeatureComponent : SettingsFeatureInternalApi {

    companion object {
        fun build() = DaggerSettingsFeatureComponent.builder().build()!!
    }
}