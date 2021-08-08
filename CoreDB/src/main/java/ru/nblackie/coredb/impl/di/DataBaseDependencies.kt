package ru.nblackie.coredb.impl.di

import android.content.Context
import ru.nblackie.coredi.FeatureDependencies

/**
 * @author tatarchukilya@gmail.com
 */
interface DataBaseDependencies : FeatureDependencies {

    fun context(): Context
}