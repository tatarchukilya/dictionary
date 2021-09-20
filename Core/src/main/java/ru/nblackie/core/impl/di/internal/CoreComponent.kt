package ru.nblackie.core.impl.di.internal

import dagger.Component
import ru.nblackie.core.api.CoreApi
import ru.nblackie.core.impl.di.CoreDependency
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Component(
    dependencies = [CoreDependency::class],
    modules = [CoreModule::class]
)
@Singleton
internal abstract class CoreComponent : CoreApi {

    companion object {
        fun build(dependencies: CoreDependency) =
            DaggerCoreComponent.builder()
                .coreDependency(dependencies)
                .build()!!
    }
}