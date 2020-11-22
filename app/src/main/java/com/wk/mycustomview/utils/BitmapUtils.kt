package com.wk.mycustomview.utils

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapUtils {

    fun zoomBitmap(srcBitmap: Bitmap?, width: Int, height: Int): Bitmap? {
        if (srcBitmap == null) return null
        val bitmapWidth = srcBitmap.width
        val bitmapHeight = srcBitmap.height
        val matrix = Matrix()
        matrix.postScale(width / bitmapWidth.toFloat(), height / bitmapHeight.toFloat())

        return Bitmap.createBitmap(srcBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true)
    }
}