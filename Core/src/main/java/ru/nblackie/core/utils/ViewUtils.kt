package ru.nblackie.core.utils

import android.R
import android.app.Activity
import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat


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
fun Activity.getPrimaryTextColor(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    //val theme: Theme = theme
    theme.resolveAttribute(attr, typedValue, true)
    val arr: TypedArray = obtainStyledAttributes(
        typedValue.data, intArrayOf(attr)
    )
    return arr.getColor(0, -1)
}

@ColorInt
fun Activity.getColorByAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}
