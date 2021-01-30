package com.wk.mycustomview.widget.twoviewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

class TwoPager(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

  private val viewConfiguration = ViewConfiguration.get(context)
  private val pageSlop = viewConfiguration.scaledPagingTouchSlop
  private val minFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity
  private val maxFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity
  private val velocityTracker = VelocityTracker.obtain()

  private val overScroller = OverScroller(context)
  private var downScrollX = 0f
  private var downX = 0f
  private var downY = 0f
  private var scrolling = false

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    measureChildren(widthMeasureSpec, heightMeasureSpec)
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft = 0
    val childTop = 0
    var childRight = width
    val childBottom = height
    for (child in children) {
      child.layout(childLeft, childTop, childRight, childBottom)
      childLeft += width
      childRight += width
    }
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()
    }
    velocityTracker.addMovement(ev)
    var result = false
    when (ev.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = ev.x
        downY = ev.y
        scrolling = false
        downScrollX = scrollX.toFloat()
      }
      MotionEvent.ACTION_MOVE -> {
        if (!scrolling) {
          val dx = ev.x - downX
          if (abs(dx) > pageSlop) {
            parent.requestDisallowInterceptTouchEvent(true)
            scrolling = true
            result = true
          }
        }
      }
    }
    return result
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
      velocityTracker.clear()
    }
    velocityTracker.addMovement(ev)
    when (ev.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = ev.x
        downY = ev.y
        scrolling = false
        downScrollX = scrollX.toFloat()
      }
      MotionEvent.ACTION_MOVE -> {
        val dx = (downX - ev.x + downScrollX).toInt()
          .coerceAtLeast(0)
          .coerceAtMost(width)
        scrollTo(dx, 0)
      }
      MotionEvent.ACTION_UP -> {
        velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity.toFloat())
        val vx = velocityTracker.xVelocity
        val scrollX = scrollX
        val percent = if (abs(vx) < minFlingVelocity) {
          if (scrollX > width / 2) {
            1
          } else {
            0
          }
        } else {
          if (vx < 0) 1 else 0
        }
        val scrollDistance = if (percent == 1) width - scrollX else -scrollX
        overScroller.startScroll(getScrollX(), 0, scrollDistance, 0)
        postInvalidateOnAnimation()
      }
    }
    return true
  }

  override fun computeScroll() {
    super.computeScroll()
    if (overScroller.computeScrollOffset()) {
      scrollTo(overScroller.currX, overScroller.currY)
      postInvalidateOnAnimation()
    }
  }

}