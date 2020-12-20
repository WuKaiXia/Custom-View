package com.wk.mycustomview.widget.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.*
import kotlin.math.max

class TagLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    // 用来存储child view的l,t,r,b
    private val childBounds = mutableListOf<Rect>()

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 起点以父布局paddingTop开始
        var heightUsed = paddingTop
        var widthUsed = 0
        var lineMaxHeight = 0
        // 起点以父布局paddingStart开始
        var lineWidthUsed = paddingStart
        // 子view可用区域需要减去父布局的padding
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec) - paddingStart - paddingEnd
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        for ((index, child) in children.withIndex()) {
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            // 判断是否需要折行
            if (specWidthMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + child.measuredWidth + child.marginStart + child.marginEnd > specWidthSize) {
                lineWidthUsed = paddingStart
                heightUsed += lineMaxHeight
                lineMaxHeight = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }
            // 为child view创建存储位置相关数据的容器
            if (index >= childBounds.size) {
                childBounds.add(Rect())
            }
            val childBound = childBounds[index]
            childBound.set(lineWidthUsed + child.marginStart, heightUsed + child.marginTop, lineWidthUsed + child.measuredWidth + child.marginStart, heightUsed + child.measuredHeight + child.marginTop)

            lineWidthUsed += child.measuredWidth + child.marginStart + child.marginEnd
            widthUsed = max(widthUsed, lineWidthUsed)
            lineMaxHeight = max(lineMaxHeight, child.measuredHeight + child.marginTop + child.marginBottom)
        }
        val selfWidth = widthUsed + paddingEnd
        val selfHeight = heightUsed + lineMaxHeight + paddingBottom
        setMeasuredDimension(selfWidth, selfHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 给child view设置位置
        for ((index, child) in children.withIndex()) {
            val childBound = childBounds[index]
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}