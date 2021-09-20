package ru.nblackie.core.impl.di

import ru.nblackie.core.api.CoreApi
import ru.nblackie.core.impl.di.internal.CoreComponent
import ru.nblackie.coredi.DependencyFeatureHolder

/**
 * @author tatarchukilya@gmail.com
 */
object CoreFeatureHolder : DependencyFeatureHolder<CoreApi, CoreDependency> {

    private var component: CoreComponent? = null

    override fun init(dependency: CoreDependency) {
        if (component == null) {
            synchronized(this) {
                if (component == null) {
                    component = CoreComponent.build(dependency)
                }
            }
        }
    }

    override fun getApi(): CoreApi {
        checkNotNull(component) { "DataBaseComponent was not initialized!" }
        return component!!
    }

    override fun reset() {
        component = null
    }
}