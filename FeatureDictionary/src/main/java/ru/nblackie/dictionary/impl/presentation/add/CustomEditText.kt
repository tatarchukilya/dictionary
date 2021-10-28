package ru.nblackie.dictionary.impl.presentation.add

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * @author Ilya Tatarchuk
 */
internal class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            clearFocus()
        return true
    }
}