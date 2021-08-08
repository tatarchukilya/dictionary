package ru.nblackie.dictionary.impl.di

import android.util.Log
import ru.nblackie.coredi.DependencyFeatureHolder
import ru.nblackie.dictionary.api.di.DictionaryApi
import ru.nblackie.dictionary.impl.di.internal.DictionaryComponent
import ru.nblackie.dictionary.impl.di.internal.DictionaryInternalApi

/**
 * @author tatarchukilya@gmail.com
 */
object DictionaryFeatureHolder : DependencyFeatureHolder<DictionaryApi, DictionaryDependencies> {

    private var component: DictionaryComponent? = null

    override fun init(dependencies: DictionaryDependencies) {
        Log.i("DictionaryComponentHolder", "init()")
        if (component == null) {
            synchronized(DictionaryFeatureHolder::class.java) {
                if (component == null) {
                    Log.i("DictionaryComponentHolder", "build")
                    component = DictionaryComponent.build(dependencies)
                }
            }
        }
    }

    override fun getApi(): DictionaryApi = getInternalApi()

    internal fun getInternalApi(): DictionaryInternalApi {
        checkNotNull(component) { "DictionaryComponent was not initialized!" }
        return component!!
    }


    override fun reset() {
        Log.i("DictionaryComponentHolder", "reset")
        component = null
    }

}