package ru.nblackie.coredb.impl.di.inner

import dagger.Component
import ru.nblackie.coredb.api.DataBaseApi
import ru.nblackie.coredb.impl.di.DataBaseDependency
import javax.inject.Singleton

/**
 * @author tatarchukilya@gmail.com
 */
@Component(
    dependencies = [DataBaseDependency::class],
    modules = [DataBaseModule::class]
)
@Singleton
internal abstract class DataBaseComponent : DataBaseApi {

    companion object {
        fun build(dependency: DataBaseDependency) =
            DaggerDataBaseComponent.builder()
                .dataBaseDependency(dependency)
                .build()!!
    }
}