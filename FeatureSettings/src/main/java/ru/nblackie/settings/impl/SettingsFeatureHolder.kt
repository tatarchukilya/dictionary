package ru.nblackie.settings.impl

import android.util.Log
import ru.nblackie.coredi.FeatureHolder
import ru.nblackie.settings.api.SettingsFeatureApi
import ru.nblackie.settings.impl.internal.SettingsFeatureComponent

/**
 * @author tatarchukilya@gmail.com
 */
object SettingsFeatureHolder: FeatureHolder<SettingsFeatureApi> {

    private var component: SettingsFeatureComponent? = null

    override fun getApi(): SettingsFeatureApi {
        Log.i("SettingsFeatureApi", "getApi()")
        if (component == null) {
            synchronized(SettingsFeatureHolder::class.java) {
                if (component == null) {
                    component = SettingsFeatureComponent.build()
                }
            }
        }
        return component!!
    }

    override fun reset() {
        component = null
    }
}