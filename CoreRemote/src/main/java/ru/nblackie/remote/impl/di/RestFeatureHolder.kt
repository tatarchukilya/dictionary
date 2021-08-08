package ru.nblackie.remote.impl.di

import android.util.Log
import ru.nblackie.coredi.FeatureHolder
import ru.nblackie.remote.api.RestApi
import ru.nblackie.remote.impl.di.internal.RestComponent

/**
 * @author tatarchukilya@gmail.com
 */
object RestFeatureHolder : FeatureHolder<RestApi> {

    private var component: RestComponent? = null

    override fun getApi(): RestApi {
        Log.i("RestFeatureHolder", "getApi")
        if (component == null) {
            synchronized(RestFeatureHolder::class.java) {
                if (component == null) {
                    Log.i("RestFeatureHolder", "build")
                    component = RestComponent.build()
                }
            }
        }
        return component!!
    }

    override fun reset() {
        Log.i("DictionaryComponentHolder", "reset")
        component = null
    }
}