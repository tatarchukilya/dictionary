package ru.nblackie.coredi

/**
 * @author tatarchukilya@gmail.com
 */
interface FeatureHolder<F : FeatureApi> {

    fun getApi(): F

    fun reset()
}
