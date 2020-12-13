package com.wk.mycustomview.widget.animate

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.px

class PointFView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var pointF = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }
    val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.LTGRAY
            strokeWidth = 25f.px
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPoint(pointF.x, pointF.y, paint)
    }
}

class PointFEvaluator : TypeEvaluator<PointF> {
    var pointF: PointF? = null
    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        val startX = startValue.x
        val endX = endValue.x
        val startY = startValue.y
        val endY = endValue.y
        if (pointF == null) {
            pointF = PointF()
        }
        pointF?.set(startX + (endX - startX) * fraction, startY + (endY - startY) * fraction)
        return pointF!!
    }
}