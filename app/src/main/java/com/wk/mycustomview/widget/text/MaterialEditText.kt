package com.wk.mycustomview.widget.text

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.wk.mycustomview.R
import com.wk.mycustomview.px

class MaterialEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    var floatingLabelFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var floatingLabelSize = 15f.px
    private var floatingLabelMargin = 10f.px
    private var useFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    setPadding(paddingLeft, (paddingTop + floatingLabelSize + floatingLabelMargin).toInt(), paddingRight, paddingBottom)
                } else {
                    setPadding(paddingLeft, (paddingTop - floatingLabelSize - floatingLabelMargin).toInt(), paddingRight, paddingBottom)
                }
            }
        }
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = floatingLabelSize
        }
    }
    private var floatingLabelVertical = 0f
    private var floatingLabelShown = false
    private val animator by lazy { ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f) }

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        floatingLabelMargin = typedArray.getDimension(R.styleable.MaterialEditText_floatingLabelMargin, floatingLabelMargin)
        floatingLabelSize = typedArray.getDimension(R.styleable.MaterialEditText_floatingLabelSize, floatingLabelSize)
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true)
        typedArray.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (floatingLabelShown && text.isNullOrEmpty()) {
            floatingLabelShown = false
            animator.reverse()
        } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
            floatingLabelShown = true
            animator.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (hint.isNullOrEmpty() || !useFloatingLabel) return
        paint.alpha = (floatingLabelFraction * 0xFF).toInt()
        floatingLabelVertical = paddingTop - floatingLabelMargin * floatingLabelFraction
        canvas.drawText(hint as String, paddingLeft.toFloat(), floatingLabelVertical, paint)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            animator.resume()
        } else {
            animator.pause()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

}