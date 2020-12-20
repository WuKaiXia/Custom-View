package com.wk.mycustomview.widget.layout

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Integer.min

class SquareView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)

    }
}