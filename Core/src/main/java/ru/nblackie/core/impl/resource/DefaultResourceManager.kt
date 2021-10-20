package ru.nblackie.core.impl.resource

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import ru.nblackie.core.R
import ru.nblackie.core.api.ResourceManager

/**
 * @author tatarchukilya@gmail.com
 */
internal class DefaultResourceManager(context: Context) : ResourceManager {

    private val context = context
    private val resources = context.resources

    override fun getString(@StringRes resId: Int): String {
        return resources.getString(resId)
    }

    @ColorInt
    override fun getColorByAttr(@AttrRes attr: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        val arr: TypedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(attr))

        return arr.getColor(0, -1)
    }

    @ColorInt
    override fun getColor(colorRes: Int): Int {
       return resources.getColor(colorRes, context.theme)
    }
}