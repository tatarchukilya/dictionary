package ru.nblackie.core

import android.content.Context
import android.util.TypedValue

/**
 * @author tatarchukilya@gmail.com
 */

fun Number.dpToPixels(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)
