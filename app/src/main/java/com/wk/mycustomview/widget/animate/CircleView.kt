package com.wk.mycustomview.widget.animate

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.px

class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
        }
    }
    var animator: AnimatorSet? = null
    var color = Color.RED
        set(value) {
            field = value
            invalidate()
        }
    var radius = 100f.px
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            animator?.resume()
        } else {
            animator?.pause()
        }
    }

    fun startAnimation(endValue: Float, color: Int) {
        if (animator == null) {
            animator = AnimatorSet()
        }
        val radiusAnimator = ObjectAnimator.ofFloat(this, "radius", endValue)
        val colorAnimator =
            ObjectAnimator.ofArgb(this, "color", Color.RED, Color.GREEN, Color.YELLOW, color)
        radiusAnimator?.let {
            it.duration = 1000
            it.repeatCount = INFINITE
            it.repeatMode = REVERSE
            it.setFloatValues(endValue)
        }

        colorAnimator?.let {
            it.duration = 1000
            it.repeatCount = INFINITE
            it.repeatMode = REVERSE
            it.setFloatValues(endValue)
        }

        animator?.playTogether(colorAnimator,radiusAnimator)
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}