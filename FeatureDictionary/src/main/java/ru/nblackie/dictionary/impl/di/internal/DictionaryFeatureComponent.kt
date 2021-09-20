package ru.nblackie.dictionary.impl.di.internal

import dagger.Component
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.impl.di.DictionaryDependency

/**
 * @author tatarchukilya@gmail.com
 */
@PerFeature
@Component(
    dependencies = [DictionaryDependency::class],
    modules = [DictionaryFeatureModule::class]
)
internal abstract class DictionaryFeatureComponent : DictionaryFeatureInternalApi {

    companion object {
        fun build(dependency: DictionaryDependency) =
            DaggerDictionaryFeatureComponent.builder()
                .dictionaryDependency(dependency)
                .build()!!
    }
}