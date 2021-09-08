package ru.nblackie.dictionary.impl.presentation.dictionary.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.max
import kotlin.math.min


/**
 * @author tatarchukilya@gmail.com
 */
class FabSlideBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency) ||
                dependency is FloatingActionButton
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

//    override fun onNestedPreScroll(
//        coordinatorLayout: CoordinatorLayout,
//        child: View,
//        target: View,
//        dx: Int,
//        dy: Int,
//        consumed: IntArray,
//        type: Int
//    ) {
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//
//    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        child.translationY =
            max(0f, min(child.height.toFloat() + child.marginBottom, child.translationY + dyConsumed))
    }
}