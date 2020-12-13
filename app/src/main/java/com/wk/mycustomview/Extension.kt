package com.wk.mycustomview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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


class AnimatorFieldDelegate<T>(private var value: T) {
    operator fun setValue(thisRef: View, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.invalidate()
    }

    operator fun getValue(thisRef: View, property: KProperty<*>): T {
        return this.value
    }
}