package ru.nblackie.coredb.impl.di.inner

import dagger.Component
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.di.DataBaseDependencies
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Component(
    dependencies = [DataBaseDependencies::class],
    modules = [DataBaseModule::class]
)
@Singleton
internal abstract class DataBaseComponent : DataBaseApi {

    companion object {
        fun build(dependencies: DataBaseDependencies) =
            DaggerDataBaseComponent.builder()
                .dataBaseDependencies(dependencies)
                .build()!!
    }
}