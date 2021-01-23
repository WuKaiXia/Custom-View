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

class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var bitmap: Bitmap = BitmapUtils.resourceToBitmap(R.drawable.flip_board, null, IMAGE_SIZE, IMAGE_SIZE, resources)!!

  private var originOffsetX = 0f
  private var originOffsetY = 0f
  private var offsetX = 0f
  private var offsetY = 0f
  private var downX = 0f
  private var downY = 0f
  private var trackingId = 0

  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
        val actionIndex = event.actionIndex
        trackingId = event.getPointerId(actionIndex)
        downX = event.getX(actionIndex)
        downY = event.getY(actionIndex)
        originOffsetX = offsetX
        originOffsetY = offsetY
      }
      MotionEvent.ACTION_POINTER_UP -> {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)
        if (pointerId == trackingId) {
          val newIndex = if (actionIndex == event.pointerCount - 1) {
            event.pointerCount - 2
          } else {
            event.pointerCount - 1
          }
          trackingId = event.getPointerId(newIndex)
          downX = event.getX(newIndex)
          downY = event.getY(newIndex)
          originOffsetX = offsetX
          originOffsetY = offsetY
        }
      }
      MotionEvent.ACTION_MOVE -> {
        offsetX = event.getX(event.findPointerIndex(trackingId)) - downX + originOffsetX
        offsetY = event.getY(event.findPointerIndex(trackingId)) - downY + originOffsetY
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