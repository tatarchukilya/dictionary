package ru.nblackie.core.api

import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes

/**
 * @author tatarchukilya@gmail.com
 */
interface ResourceManager {

    fun getString(@StringRes resId: Int): String

    fun getColorByAttr(@AttrRes attr: Int): Int

    @ColorInt
    fun getColor(colorRes: Int): Int

}