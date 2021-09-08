package ru.nblackie.core.utils

import android.animation.Animator
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
