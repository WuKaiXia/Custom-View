package com.wk.mycustomview.widget.multitouch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wk.mycustomview.R
import com.wk.mycustomview.px
import com.wk.mycustomview.utils.BitmapUtils

private val IMAGE_SIZE = 200f.px.toInt()

class MultiTouchView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var bitmap: Bitmap = BitmapUtils.resourceToBitmap(R.drawable.flip_board, null, IMAGE_SIZE, IMAGE_SIZE, resources)!!

  private var originOffsetX = 0f
  private var originOffsetY = 0f
  private var offsetX = 0f
  private var offsetY = 0f
  private var downX = 0f
  private var downY = 0f

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val focusX: Float
    val focusY: Float
    var sumX = 0f
    var sumY = 0f
    var pointerCount = event.pointerCount
    val isPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
    for (i in 0 until pointerCount) {
      if (!(isPointerUp && event.actionIndex == i)) {
        sumX += event.getX(i)
        sumY += event.getY(i)
      }
    }
    if (isPointerUp) {
      pointerCount--
    }
    focusX = sumX / pointerCount
    focusY = sumY / pointerCount
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP-> {
        downX = focusX
        downY = focusY
        originOffsetX = offsetX
        originOffsetY = offsetY
      }
      MotionEvent.ACTION_MOVE -> {
        offsetX = focusX - downX + originOffsetX
        offsetY = focusY - downY + originOffsetY
        invalidate()
      }
    }
    return true
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
  }
}