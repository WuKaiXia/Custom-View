package com.wk.mycustomview.widget

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isInvisible
import com.wk.mycustomview.R
import com.wk.mycustomview.px

private val IMAGE_WIDTH = 200f.px.toInt()

class AvatarView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val xfermode by lazy { PorterDuffXfermode(PorterDuff.Mode.SRC_IN) }
    private val paint by lazy { Paint(ANTI_ALIAS_FLAG) }
    private var srcId = -1
    private var divideWidth = 0f
    private var imageWidth = 0f
    private var bitmap: Bitmap? = null
    private lateinit var bounds: RectF
    private var imgLeft: Float = 0f
    private var imgTop: Float = 0f
    private var imgRight: Float = 0f
    private var imgBottom: Float = 0f

    init {
        context?.let {
            val typedValue = it.obtainStyledAttributes(attrs, R.styleable.AvatarView)
            srcId = typedValue.getResourceId(R.styleable.AvatarView_src, srcId)
            divideWidth =
                typedValue.getDimension(R.styleable.AvatarView_avatar_divide_width, divideWidth)
            typedValue.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = IMAGE_WIDTH
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = IMAGE_WIDTH
        }
        imageWidth = widthSize - divideWidth * 2
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bounds = RectF(0f, 0f, width.toFloat(), width.toFloat())
        imgLeft = divideWidth
        imgTop = divideWidth
        imgRight = width - divideWidth
        imgBottom = width - divideWidth
        resourceToBitmap(srcId)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (divideWidth > 0) {
                it.drawOval(bounds, paint)
            }
            val count = it.saveLayer(bounds, null)
            it.drawOval(imgLeft, imgTop, imgRight, imgBottom, paint)
            paint.xfermode = xfermode

            bitmap?.let { bitmap ->
                it.drawBitmap(bitmap, imgLeft, imgTop, paint)
            }
            paint.xfermode = null

            it.restoreToCount(count)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap?.recycle()
    }

    private fun resourceToBitmap(srcId: Int) {
        if (srcId == -1) return
        var tempBitmap = bitmap
        tempBitmap?.recycle()
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        tempBitmap = BitmapFactory.decodeResource(resources, srcId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = imageWidth.toInt()
        bitmap = BitmapFactory.decodeResource(resources, srcId, options)
        tempBitmap?.recycle()
    }

    fun setSrc(id: Int) {
        resourceToBitmap(id)
        invalidate()
    }
}