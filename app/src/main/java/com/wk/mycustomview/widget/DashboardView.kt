package com.wk.mycustomview.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.wk.mycustomview.px
import kotlin.math.cos
import kotlin.math.sin

private const val OPEN_ANGLE = 120f
private const val MARK = 10f
private val RADIUS = 150f.px
private val LENGTH = 120f.px
private val DASH_WIDTH = 2f.px
private val DASH_LENGTH = 10f.px
private val DASH_PHASE = 20f

class DashboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5f.px
            style = Paint.Style.STROKE
        }
    }

    private val path by lazy { Path() }
    private val dash by lazy {
        Path().apply {
            addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CCW)
        }
    }
    private val pathMeasure by lazy { PathMeasure() }
    private var mark = MARK
        set(value) {
            field = value
            invalidate()
        }
    private var animator: ObjectAnimator? = null
    private lateinit var pathEffect: PathDashPathEffect

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((RADIUS * 2 + 50).toInt(), (RADIUS * 2 + 50).toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.reset()
        path.addArc(
            width / 2 - RADIUS,
            height / 2f - RADIUS,
            width / 2f + RADIUS,
            height / 2f + RADIUS,
            OPEN_ANGLE / 2 + 90,
            360 - OPEN_ANGLE
        )
        pathMeasure.setPath(path, false)

        pathEffect = PathDashPathEffect(
            dash,
            (pathMeasure.length - DASH_WIDTH) / DASH_PHASE,
            0f,
            PathDashPathEffect.Style.ROTATE
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawPath(path, paint)

            paint.pathEffect = pathEffect
            it.drawPath(path, paint)


            paint.pathEffect = null

            it.drawLine(
                width / 2f,
                height / 2f,
                LENGTH * cos(markToRadians(mark)).toFloat() + width / 2f,
                LENGTH * sin(
                    markToRadians(
                        mark
                    )
                ).toFloat() + height / 2f,
                paint
            )
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            startAnimator()
        } else {
            stopAnimator()
        }
    }

    private fun stopAnimator() {
        animator.action { it.pause() }
    }

    private fun cancelAnimator() {
        animator.action { it.cancel() }
    }

    private fun startAnimator() {
        animator.action { it.resume() }
    }

    private inline fun ObjectAnimator?.action(action:(animator:ObjectAnimator)->Unit) {
        this?.let { action(it) } ?: setMyAnimation()
    }

    private fun setMyAnimation() {
        animator = ObjectAnimator.ofFloat(this, "mark", 0f, 10f).apply {
            duration = 3000
            interpolator = AccelerateInterpolator()
            start()
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimator()
    }

    private fun markToRadians(mark: Float) =
        Math.toRadians((90 + OPEN_ANGLE / 2f + (360 - OPEN_ANGLE) / 20f * mark).toDouble())

}