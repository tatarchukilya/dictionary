package ru.nblackie.coredi

/**
 * @author tatarchukilya@gmail.com
 */
interface DependencyFeatureHolder<F : FeatureApi, D : FeatureDependencies> : FeatureHolder<F> {

    fun init(dependencies: D)
}