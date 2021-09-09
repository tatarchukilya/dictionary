package ru.nblackie.dictionary.impl.di

import android.util.Log
import dagger.internal.Preconditions.checkNotNull
import ru.nblackie.coredi.DependencyFeatureHolder
import ru.nblackie.dictionary.api.di.DictionaryFeatureApi
import ru.nblackie.dictionary.impl.di.internal.DictionaryFeatureComponent
import ru.nblackie.dictionary.impl.di.internal.DictionaryFeatureInternalApi

/**
 * @author tatarchukilya@gmail.com
 */
object DictionaryFeatureHolder : DependencyFeatureHolder<DictionaryFeatureApi, DictionaryDependencies> {

    private var component: DictionaryFeatureComponent? = null

    override fun init(dependencies: DictionaryDependencies) {
        Log.i("DictionaryComponentHolder", "init")
        if (component == null) {
            synchronized(DictionaryFeatureHolder::class.java) {
                if (component == null) {
                    Log.i("DictionaryComponentHolder", "build")
                    component = DictionaryFeatureComponent.build(dependencies)
                }
            }
        }
    }

    override fun getApi(): DictionaryFeatureApi = getInternalApi()

    internal fun getInternalApi(): DictionaryFeatureInternalApi {
        checkNotNull(component, "DictionaryComponent was not initialized!")
        return component!!
    }


    override fun reset() {
        Log.i("DictionaryComponentHolder", "reset")
        component = null
    }

}