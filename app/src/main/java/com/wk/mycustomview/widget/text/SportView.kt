package com.wk.mycustomview.widget.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.wk.mycustomview.px

class SportView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val fontMetrics = Paint.FontMetrics()
    private val bounds = Rect()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sportText = "30adjpo"

    private var sportTextSize = 60f.px
    private var radius = 200f.px
    private var strokeWidth = 10f.px

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = radius.coerceAtMost(width / 3f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        paint.color = Color.GRAY
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        paint.color = Color.GREEN
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            width / 2 - radius,
            height / 2 - radius,
            width / 2 + radius,
            height / 2 + radius,
            -90f,
            120f,
            false,
            paint
        )

        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = sportTextSize
        paint.color = Color.GREEN
        // 适合静态文字居中
        paint.getTextBounds(sportText, 0, sportText.length, bounds)
        canvas.drawText(
            sportText,
            width / 2f,
            height / 2f - (bounds.bottom + bounds.top) / 2f,
            paint
        )


//        // 适合动态文字居中
//        paint.getFontMetrics(fontMetrics)
//        paint.color = Color.RED
//        canvas.drawText(
//            sportText,
//            width / 2f,
//            height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f,
//            paint
//        )
//
//        // 文字左侧贴边
//        paint.textAlign = Paint.Align.LEFT
//        canvas.drawText(
//            sportText,
//            -bounds.left.toFloat(),
//            0f,
//            paint
//        )
//
//        // 文字顶部贴边，bounds.top:完全贴边；fontMetrics.top: 文字间有间隙，适合阅读类文本
//        canvas.drawText(
//            sportText,
//            -bounds.left.toFloat(),
//            -bounds.top.toFloat(),
//            paint
//        )
    }
}