package com.wk.mycustomview.widget.xfermode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.wk.mycustomview.R
import com.wk.mycustomview.utils.BitmapUtils.zoomBitmap
import kotlin.math.abs


class ScratchCardView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val xfermode by lazy { PorterDuffXfermode(PorterDuff.Mode.SRC_OUT) }
    private val mBitmapPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 45f
            style = Paint.Style.STROKE
            color = Color.RED
        }
    }
    private var mBmpText: Bitmap? = null

    private var mBmpSrc: Bitmap? = null

    private var mBmpDst: Bitmap? = null

    private val mPath by lazy { Path() }
    private lateinit var mBitmapCanvas: Canvas
    private var mPreX = 0f
    private var mPreY = 0f
    private val mPathMeasure by lazy { PathMeasure(mPath, false) }
    private var mTotalPathLength = 0f
    private var isCompleted = false
    private var mSwipeCompletePercentage = 70f

    private val mTask = Runnable {
        mBmpDst?.let {
            var wipeArea = 0
            val w = width
            val h = height
            val totalArea = w * h
            val pixels = IntArray(totalArea)
            it.getPixels(pixels, 0, w, 0, 0, w, h)
            for (i in 0 until w) {
                for (j in 0 until h) {
                    val index = i + j * w
                    if (pixels[index] != 0) {
                        wipeArea++
                    }
                }
            }
            Log.e("Scratch", "wipeArea::${wipeArea}")
            if (wipeArea > 0 && totalArea > 0) {
                val percent = (wipeArea * 100) / totalArea
                if (percent >= mSwipeCompletePercentage) {
                    isCompleted = true
                    postInvalidate()
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBmpText =
            zoomBitmap(resourceToBitmap(R.drawable.scratch_card_text, mBmpText), width, height)
        mBmpSrc = zoomBitmap(resourceToBitmap(R.drawable.scratch, mBmpSrc), width, height)
        mBmpDst = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888
        )
        mBitmapCanvas = Canvas(mBmpDst!!)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            mBmpText?.let { it1 -> it.drawBitmap(it1, 0f, 0f, mBitmapPaint) }
            mPathMeasure.setPath(mPath, false)
            mTotalPathLength += mPathMeasure.length

            if (isCompleted) {
                return@let
            }

            val layerId = it.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

            mBitmapCanvas.drawPath(mPath, mBitmapPaint)

            mBmpDst?.let { it1 -> it.drawBitmap(it1, 0f, 0f, mBitmapPaint) }

            mBitmapPaint.xfermode = xfermode
            mBmpSrc?.let { it1 -> it.drawBitmap(it1, 0f, 0f, mBitmapPaint) }
            mBitmapPaint.xfermode = null

            it.restoreToCount(layerId)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBmpText?.recycle()
        mBmpDst?.recycle()
        mBmpSrc?.recycle()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isCompleted) {
            parent?.requestDisallowInterceptTouchEvent(true)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreX = event.x
                mPreY = event.y
                mPath.moveTo(mPreX, mPreY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                mPath.quadTo(mPreX, mPreY, endX, endY)
                mPreY = event.y
                mPreX = event.x
                post(mTask)
            }
        }
        if (!isCompleted) {
            postInvalidate()
        }
        return super.onTouchEvent(event)
    }

    private fun resourceToBitmap(srcId: Int, lastBitmap: Bitmap?): Bitmap? {
        if (srcId == -1) return null
        var tempBitmap = lastBitmap
        tempBitmap?.recycle()
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        tempBitmap = BitmapFactory.decodeResource(resources, srcId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth

        options.outHeight = height
        options.outWidth = width

        options.inTargetDensity = width
        val bitmap = BitmapFactory.decodeResource(resources, srcId, options)
        tempBitmap?.recycle()
        return bitmap
    }
}