package com.wk.mycustomview.widget.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.px

private val RADIUS = 100f.px

class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = (RADIUS * 2).toInt()


        setMeasuredDimension(
            resolveSize(size, widthMeasureSpec),
            resolveSize(size, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, paint)
    }

}