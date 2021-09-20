package ru.nblackie.coredb.impl.di

import android.util.Log
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.di.inner.DataBaseComponent
import ru.nblackie.coredi.DependencyFeatureHolder

/**
 * @author tatarchukilya@gmail.com
 */
object DataBaseFeatureHolder : DependencyFeatureHolder<DataBaseApi, DataBaseDependency> {

    private var component: DataBaseComponent? = null

    override fun init(dependency: DataBaseDependency) {
        Log.i("DataBaseFeatureHolder", "init()")
        if (component == null) {
            synchronized(DataBaseFeatureHolder::class.java) {
                if (component == null) {
                    Log.i("DataBaseFeatureHolder", "build")
                    component = DataBaseComponent.build(dependency)
                }
            }
        }
    }

    override fun getApi(): DataBaseApi {
        checkNotNull(component) { "DataBaseComponent was not initialized!" }
        return component!!
    }

    override fun reset() {
        component = null
    }
}