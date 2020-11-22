package com.wk.mycustomview.widget.canvas

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.wk.mycustomview.R
import com.wk.mycustomview.utils.BitmapUtils

class CanvasView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val mCamera by lazy { Camera() }
    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private var mBitmap: Bitmap?
    private var mScale: Float
    var isCamera = false
    private var degree = 30f
    private var cameraDegree = 60f

    init {
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.CanvasView)
        val bitmap = typedValue.getDrawable(R.styleable.CanvasView_cv_src) as? BitmapDrawable
        mScale = typedValue.getFloat(R.styleable.CanvasView_cv_scale, 1f)
        mBitmap = bitmap?.bitmap ?: BitmapFactory.decodeResource(resources, R.drawable.flip_board)
        typedValue.recycle()
        mCamera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (mBitmap?.height ?: 0)  + paddingBottom + paddingTop
        }

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (mBitmap?.width ?: 0)  + paddingStart + paddingEnd
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isCamera) {
            mScale = 1f
            mBitmap?.let {
                val left = (width - it.width) / (mScale * mScale * 2)
                val top = (height - it.height) / (mScale * mScale * 2)
                canvas.save()
                canvas.scale(mScale, mScale)
                canvas.rotate(90f, width / 2f / mScale, height / 2f / mScale)
                canvas.drawBitmap(it, left, top, mPaint)
                canvas.restore()
            }
        } else {

            mBitmap?.let {
                val left = (width - it.width * mScale) / (mScale * 2)
                val top = (height - it.height * mScale) / (mScale * 2)

                canvas.save()
                canvas.scale(mScale, mScale)
                canvas.translate(left + it.width / 2, top + it.height / 2)
                canvas.rotate(-degree)
                canvas.clipRect(-it.width, -it.height,it.width, 0)
                canvas.rotate(degree)
                canvas.translate(
                    -(left + it.width / 2),
                    -(top + it.height / 2)
                )

                canvas.drawBitmap(it, left, top, mPaint)
                canvas.restore()


                canvas.save()
                mCamera.save()
                canvas.scale(mScale, mScale)
                canvas.translate(left + it.width / 2, top + it.height / 2)
                canvas.rotate(-degree)
                mCamera.rotateX(cameraDegree)
                mCamera.applyToCanvas(canvas)
                canvas.clipRect(-it.width, 0,it.width, it.height)
                canvas.rotate(degree)
                canvas.translate(
                    -(left + it.width / 2),
                    -(top + it.height / 2)
                )

                canvas.drawBitmap(it, left, top, mPaint)
                mCamera.restore()
                canvas.restore()
            }

        }


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBitmap?.recycle()
    }
}