package com.wk.mycustomview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.util.TypedValue

val Float.px
    get() =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

fun ValueAnimator.repeatMode() {
    this.repeatMode = ValueAnimator.REVERSE
    repeatCount = ValueAnimator.INFINITE
}
