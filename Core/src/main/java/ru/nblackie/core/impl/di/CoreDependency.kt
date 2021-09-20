package ru.nblackie.core.impl.di

import android.content.Context
import ru.nblackie.coredi.FeatureDependency

/**
 * @author tatarchukilya@gmail.com
 */
interface CoreDependency: FeatureDependency {

    fun context(): Context
}