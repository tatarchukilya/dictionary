package ru.nblackie.core.utils

import android.animation.Animator
import androidx.transition.Transition

/**
 * @author tatarchukilya@gmail.com
 */

abstract class EndAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
        super.onAnimationStart(animation, isReverse)
    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        super.onAnimationEnd(animation, isReverse)
    }

    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationRepeat(p0: Animator?) {

    }
}

abstract class EndTransitionListener : Transition.TransitionListener {

    override fun onTransitionStart(transition: Transition) {

    }

    override fun onTransitionCancel(transition: Transition) {

    }

    override fun onTransitionPause(transition: Transition) {

    }

    override fun onTransitionResume(transition: Transition) {

    }
}

abstract class StartEndTransitionListener() : Transition.TransitionListener {
    override fun onTransitionCancel(transition: Transition) {

    }

    override fun onTransitionPause(transition: Transition) {

    }

    override fun onTransitionResume(transition: Transition) {

    }
}