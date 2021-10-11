package ru.nblackie.core.impl.utils

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.view.Display
import android.util.DisplayMetrics

import android.view.WindowInsets

import android.view.WindowMetrics

import android.os.Build

/**
 * @author tatarchukilya@gmail.com
 */

fun EditText.showKeyboard() {
    requestFocus()
    ContextCompat.getSystemService(context, InputMethodManager::class.java)
        ?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    //Find the currently focused view, so we can grab the correct window token from it.
    var view: View? = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@ColorInt
fun Context.getColorByAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    val arr: TypedArray = obtainStyledAttributes(
        typedValue.data, intArrayOf(attr)
    )
    return arr.getColor(0, -1)
}

fun Context.getTintDrawableByAttr(@DrawableRes drawableResId: Int, @AttrRes attrResId: Int): Drawable? {
    val drawable = ResourcesCompat.getDrawable(resources, drawableResId, theme)
    if (drawable != null) {
        val color = this.getColorByAttr(attrResId)
        DrawableCompat.setTint(drawable, color)
    }
    return drawable
}

fun Activity.screenHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
        // val insets: Insets = windowMetrics.windowInsets
        //     .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds
        windowMetrics.bounds.height()
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

