package com.wk.mycustomview.widget.multitouch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.core.util.forEach
import androidx.core.util.getOrDefault
import com.wk.mycustomview.px

class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  val sparseArray = SparseArray<Path>()

  init {
    paint.apply {
      style = Paint.Style.STROKE
      strokeCap = Paint.Cap.ROUND
      strokeWidth = 4f.px
      color = Color.BLACK
      strokeJoin = Paint.Join.ROUND
    }
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.actionMasked) {
      MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)
        val path = sparseArray.getOrDefault(pointerId, Path())
        path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
        sparseArray.append(pointerId, path)
        invalidate()
      }
      MotionEvent.ACTION_MOVE -> {
        for (i in 0 until sparseArray.size()) {
          val pointerId = event.getPointerId(i)
          val path = sparseArray.get(pointerId)
          path.lineTo(event.getX(i), event.getY(i))
        }
        invalidate()
      }
      MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)
        sparseArray.remove(pointerId)
        invalidate()
      }
    }

    return true
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    sparseArray.forEach { _, value ->
      canvas.drawPath(value, paint)
    }
  }
}