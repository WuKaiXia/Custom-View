package com.wk.mycustomview.widget.text

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.R
import com.wk.mycustomview.px
import com.wk.mycustomview.utils.BitmapUtils

class MultiLineTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val bitmapWidth = 150f.px.toInt()
    private val text =
        "Nowadays, more and more middle-aged people are suffering from insomnia, as life for the middle-aged is stressful indeed. For one thing, as they are the backbones of their companies, they have plenty of things to do at work. And they usually have to work overtime. For another, they have to take great responsibilities at home, for their aged parents need to be supported and their little children need to be brought up. That's why they don't have enough time to have a good rest.To sleep well, some of them often take sleeping pills. I don't think it's good for them to do so, because the sleeping pills can only get rid of the sign of insomnia, not the cause. And taking too much sleeping pills is bad for their health.Therefore, I suggest those middle-aged people should know how to relax themselves and rest their brains. Taking a simple work after supper will be beneficial for them. Besides, they can have a glass of milk before going to bed, which can be helpful for their sleep."

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 13f.px
    }
    private val measureWidth = floatArrayOf(0f)
    private val fontMetrics = Paint.FontMetrics()

    private var bitmap: Bitmap? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = BitmapUtils.resourceToBitmap(
            R.drawable.flip_board,
            null,
            bitmapWidth,
            bitmapWidth,
            resources
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap?.let {
            canvas.drawBitmap(
                it,
                width.toFloat() - bitmapWidth,
                height / 2f - bitmapWidth,
                paint
            )
        }

        // 绘制多行文字
        paint.getFontMetrics(fontMetrics)
        var count: Int
        var start = 0
        var verticalOffset = -fontMetrics.top
        while (start < text.length) {
            val maxWidth =
                if (verticalOffset + fontMetrics.bottom > height / 2f - bitmapWidth && verticalOffset + fontMetrics.top < height / 2f) {
                    width - bitmapWidth.toFloat()
                } else {
                    width.toFloat()
                }
            count =
                paint.breakText(text, start, text.length, true, maxWidth, measureWidth)
            canvas.drawText(
                text,
                start,
                start + count,
                0f,
                verticalOffset,
                paint
            )
            start += count
            verticalOffset += paint.fontSpacing
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap?.recycle()
    }
}