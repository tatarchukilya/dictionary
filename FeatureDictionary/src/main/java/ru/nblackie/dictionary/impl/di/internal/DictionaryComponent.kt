package ru.nblackie.dictionary.impl.di.internal

import dagger.Component
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.impl.di.DictionaryDependencies

/**
 * @author tatarchukilya@gmail.com
 */
@PerFeature
@Component(
    dependencies = [DictionaryDependencies::class],
    modules = [DictionaryFeatureModule::class]
)
internal abstract class DictionaryComponent : DictionaryInternalApi {

    companion object {
        fun build(dependencies: DictionaryDependencies) =
            DaggerDictionaryComponent.builder()
                .dictionaryDependencies(dependencies)
                .build()!!
    }
}