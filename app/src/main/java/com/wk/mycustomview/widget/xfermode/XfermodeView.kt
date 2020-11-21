package com.wk.mycustomview.widget.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.px

class XfermodeView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var mode = PorterDuff.Mode.SRC
        set(value) {
            field = value
            mXfermode = PorterDuffXfermode(value)
            invalidate()
        }
    private var mXfermode = PorterDuffXfermode(mode)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF(150f.px, 50f.px, 300f.px, 200f.px)
    private val circleBitmap =
        Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)
    private val squareBitmap =
        Bitmap.createBitmap(150f.px.toInt(), 150f.px.toInt(), Bitmap.Config.ARGB_8888)

    init {
        val canvas = Canvas(circleBitmap)
        paint.color = Color.parseColor("#D81B60")
        canvas.drawOval(50f.px, 0f.px, 150f.px, 100f.px, paint)
        paint.color = Color.parseColor("#2196F3")
        canvas.setBitmap(squareBitmap)
        canvas.drawRect(0f.px, 50f.px, 100f.px, 150f.px, paint)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = canvas.saveLayer(bounds, null)
        // dst
        canvas.drawBitmap(circleBitmap, 150f.px, 50f.px, paint)
        paint.xfermode = mXfermode
        // source
        canvas.drawBitmap(squareBitmap, 150f.px, 50f.px, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)
    }
}