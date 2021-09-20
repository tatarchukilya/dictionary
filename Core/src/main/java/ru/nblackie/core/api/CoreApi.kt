package ru.nblackie.core.api

import ru.nblackie.coredi.FeatureApi

/**
 * @author tatarchukilya@gmail.com
 */
interface CoreApi : FeatureApi{

    fun resourceManager(): ResourceManager
}