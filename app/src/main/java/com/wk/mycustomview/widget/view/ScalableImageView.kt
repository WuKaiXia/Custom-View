package com.wk.mycustomview.widget.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isInvisible
import com.wk.mycustomview.R
import com.wk.mycustomview.px
import com.wk.mycustomview.utils.BitmapUtils

private val IMAGE_SIZE = 300f.px.toInt()
private val EXTRA_FRACTION = 2f

class ScalableImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG);
    private var bitmap: Bitmap = BitmapUtils.resourceToBitmap(R.drawable.flip_board, null, IMAGE_SIZE, IMAGE_SIZE, resources)!!

    private val runner = ScalableFlingRunner()

    private val scaleGestureListener = ScaleGestureListener()
    private val scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

    // 手势辅助类
    private val gestureListener = ScalableGestureListener()
    private val gestureDetector = GestureDetectorCompat(context, gestureListener)

    // fling 滑动的工具类
    private val overScroller = OverScroller(context)

    private var big = false
    private var smallScale = 0f
    private var bigScale = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val animator = ObjectAnimator.ofFloat(this, "currentScale", smallScale, bigScale)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        if (bitmap.width / bitmap.height.toFloat() > width / height.toFloat()) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat() * EXTRA_FRACTION
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat() * EXTRA_FRACTION
        }
        currentScale = smallScale
        animator.setFloatValues(smallScale, bigScale)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            gestureDetector.onTouchEvent(event)
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleFraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    inner class ScalableGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent, currentEvent: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (big || currentScale > smallScale) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
                invalidate()
            }
            return false
        }

        override fun onFling(e1: MotionEvent?, currentEvent: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (big) {
                overScroller.fling(
                    offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2).toInt(),
                    ((bitmap.width * bigScale - width) / 2).toInt(),
                    (-(bitmap.height * bigScale - height) / 2).toInt(),
                    ((bitmap.height * bigScale - height) / 2).toInt(),
                )
                ViewCompat.postOnAnimation(this@ScalableImageView, runner)
            }
            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            big = !big
            if (big) {
                // 跟随手指点击的位置来放大
                offsetX = (e.x - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (e.y - height / 2f) * (1 - bigScale / smallScale)
                fixOffset()
                animator.setFloatValues(currentScale, bigScale)
                animator.start()
            } else {
                animator.setFloatValues(smallScale, currentScale)
                animator.reverse()
            }
            return super.onDoubleTap(e)
        }
    }

    // 校验偏移量的范围
    private fun fixOffset() {
        offsetX = offsetX.coerceAtLeast(-(bitmap.width * bigScale - width) / 2f)
            .coerceAtMost((bitmap.width * bigScale - width) / 2f)
        offsetY = offsetY.coerceAtLeast(-(bitmap.height * bigScale - height) / 2f)
            .coerceAtMost((bitmap.height * bigScale - height) / 2f)
    }

    inner class ScalableFlingRunner : Runnable {
        override fun run() {
            if (overScroller.computeScrollOffset()) {
                offsetX = overScroller.currX.toFloat()
                offsetY = overScroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }

    inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempScale = currentScale * detector.scaleFactor
            return if (tempScale < smallScale || tempScale > bigScale) {
                false
            } else {
                big = tempScale > smallScale
                currentScale = tempScale
                true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
            offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }
    }
}