package ru.nblackie.coredb.impl.di

import android.content.Context
import ru.nblackie.coredi.FeatureDependency

/**
 * @author tatarchukilya@gmail.com
 */
interface DataBaseDependency : FeatureDependency {

    fun context(): Context
}