package com.wk.mycustomview.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    fun resourceToBitmap(
        srcId: Int,
        lastBitmap: Bitmap?,
        width: Int,
        height: Int,
        resources: Resources
    ): Bitmap? {
        if (srcId == -1) return null
        var tempBitmap = lastBitmap
        tempBitmap?.recycle()
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        tempBitmap = BitmapFactory.decodeResource(resources, srcId, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth

        options.outHeight = height
        options.outWidth = width

        options.inTargetDensity = width
        val bitmap = BitmapFactory.decodeResource(resources, srcId, options)
        tempBitmap?.recycle()
        return bitmap
    }
}