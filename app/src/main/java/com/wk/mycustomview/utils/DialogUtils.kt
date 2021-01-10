package com.wk.mycustomview.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import com.wk.mycustomview.R

object DialogUtils {

    @SuppressLint("PrivateApi")
    fun getDialogRootView(activity: Activity) {
        val windowManager = activity.windowManager

        val wmiClass = Class.forName("android.view.WindowManagerImpl")
        val mGlobalField = wmiClass.getDeclaredField("mGlobal")
        mGlobalField.isAccessible = true

        val wmgClass = Class.forName("android.view.WindowManagerGlobal")
        val mViewsField = wmgClass.getDeclaredField("mViews")
        mViewsField.isAccessible = true

        val mViews = mViewsField.get(mGlobalField.get(windowManager))

        if (mViews is List<*>) {
            mViews.forEach {
                if (it is View) {
                    it.setBackgroundResource(R.color.black)
                }
                Log.e(this.javaClass.simpleName, "view:: $it")
            }
        }
    }
}