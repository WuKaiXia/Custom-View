package com.wk.mycustomview.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.*

private const val COLUMNS = 2
private const val ROWS = 3

class DragHelperGridView(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

  private val dragHelper = ViewDragHelper.create(this, DragCallback())
  private var currentDragState = STATE_IDLE
  private var touchedView: View? = null

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)
    val childWidth = widthSize / COLUMNS
    val childHeight = heightSize / ROWS
    measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY))
    setMeasuredDimension(widthSize, heightSize)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var childLeft: Int
    var childTop: Int
    val childWidth = width / COLUMNS
    val childHeight = height / ROWS
    for ((index, child) in children.withIndex()) {
      childLeft = index % 2 * childWidth
      childTop = index / 2 * childHeight
      child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
    }
  }

  override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
    return dragHelper.shouldInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    dragHelper.processTouchEvent(event)
    return true
  }

  override fun computeScroll() {
    super.computeScroll()
    if (dragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this)
    }
  }

  private inner class DragCallback : ViewDragHelper.Callback() {
    var capturedLeft = 0f
    var capturedTop = 0f

    // 是否可以drag ，false 不可以
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return currentDragState == STATE_IDLE
    }

    /**
     * Called when the drag state changes. See the <code>STATE_*</code> constants
     * for more information.
     *
     * @param state The new drag state
     *
     * @see #STATE_IDLE
     * @see #STATE_DRAGGING
     * @see #STATE_SETTLING
     */
    override fun onViewDragStateChanged(state: Int) {
      super.onViewDragStateChanged(state)
      currentDragState = state
      when (state) {
        STATE_IDLE -> {
          capturedLeft = 0f
          capturedTop = 0f
          elevation = 1f
          touchedView?.elevation = 0f
          touchedView = null
        }
        STATE_DRAGGING -> {

        }
        STATE_SETTLING -> {
        }
      }
    }

    // 开始拖动view时的回调
    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
      super.onViewCaptured(capturedChild, activePointerId)
      touchedView = capturedChild
      capturedChild.elevation = elevation + 1
      capturedLeft = capturedChild.left.toFloat()
      capturedTop = capturedChild.top.toFloat()
    }

    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
      return left
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return top
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      super.onViewReleased(releasedChild, xvel, yvel)
      dragHelper.settleCapturedViewAt(capturedLeft.toInt(), capturedTop.toInt())
      postInvalidateOnAnimation()
    }
  }
}