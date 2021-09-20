package ru.nblackie.coredi

/**
 * @author tatarchukilya@gmail.com
 */
interface DependencyFeatureHolder<F : FeatureApi, D : FeatureDependency> : FeatureHolder<F> {

    fun init(dependency: D)
}