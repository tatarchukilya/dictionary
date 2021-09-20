package ru.nblackie.core.api

import androidx.annotation.StringRes

/**
 * @author tatarchukilya@gmail.com
 */
interface ResourceManager {

    fun getString(@StringRes resId: Int): String
}