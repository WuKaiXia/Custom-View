package com.wk.mycustomview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.R
import com.wk.mycustomview.px
import kotlin.math.cos
import kotlin.math.sin

private val ANGLES = floatArrayOf(60f, 90f, 150f, 60f)
private val COLORS = listOf(
    Color.parseColor("#C2185B"),
    Color.parseColor("#00ACC1"),
    Color.parseColor("#558B2F"),
    Color.parseColor("#5D4037")
)
private val OFFSET_LENGTH = 20f.px

class PieView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint by lazy { Paint(ANTI_ALIAS_FLAG) }

    private var mRadius = 150f.px

    private var index = 0

    private var isLeft = true

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.PieView)
        mRadius = typedArray?.getDimension(R.styleable.PieView_radius, mRadius) ?: mRadius
        typedArray?.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(
                (2 * (mRadius + OFFSET_LENGTH)).toInt(),
                (2 * (mRadius + OFFSET_LENGTH)).toInt()
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var startAngle = 0f
        for (i in COLORS.indices) {
            val angle = ANGLES[i]
            if (index == i) {
                canvas?.save()
                canvas?.translate(
                    OFFSET_LENGTH * cos(Math.toRadians((startAngle + angle / 2f).toDouble())).toFloat(),
                    OFFSET_LENGTH * sin(Math.toRadians((startAngle + angle / 2f).toDouble())).toFloat()
                )
            }
            paint.color = COLORS[i]
            canvas?.drawArc(
                width / 2f - mRadius,
                height / 2f - mRadius,
                width / 2f + mRadius,
                height / 2f + mRadius,
                startAngle,
                angle,
                true,
                paint
            )
            startAngle += angle
            if (index == i) {
                canvas?.restore()
            }
        }
    }

    fun switchPie() {
        if (isLeft) {
            index++
        } else {
            index--
        }
        if (index <= 0) {
            index = 0
            isLeft = true
        } else if (index >= COLORS.size - 1) {
            isLeft = false
            index = COLORS.size - 1
        }
        invalidate()
    }

}