package ru.nblackie.dictionary.impl.presentation.dictionary.search

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * @author tatarchukilya@gmail.com
 */
class TempBehavior(context: Context?, attrs: AttributeSet?) :
    AppBarLayout.ScrollingViewBehavior(context, attrs) {

    private var crTrY = Int.MIN_VALUE

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.i("<>child", child.javaClass.simpleName)
        Log.i("<>dependency", dependency.javaClass.simpleName)
        if (crTrY == Int.MIN_VALUE) {
            crTrY = dependency.top
            return true
        }
        if (crTrY != dependency.top) {
        crTrY -= dependency.top
        val layoutParams = child.layoutParams as ViewGroup.MarginLayoutParams
        val top = layoutParams.topMargin + crTrY
        layoutParams.setMargins(
            layoutParams.leftMargin,
            top, layoutParams.rightMargin, layoutParams.bottomMargin
        )
        dependency.requestLayout()
        return true
    }
    return false
    //return super.onDependentViewChanged(parent, child, dependency)
}
}