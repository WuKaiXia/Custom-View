package com.wk.mycustomview

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.SystemClock
import android.util.Log

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.e(activity.javaClass.simpleName, "onActivityCreated:${System.currentTimeMillis()}")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.e(activity.javaClass.simpleName, "onActivityStarted:${System.currentTimeMillis()}")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.e(activity.javaClass.simpleName, "onActivityResumed:${System.currentTimeMillis()}")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.e(activity.javaClass.simpleName, "onActivityPaused:${System.currentTimeMillis()}")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.e(activity.javaClass.simpleName, "onActivityStopped:${System.currentTimeMillis()}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.e(activity.javaClass.simpleName, "onActivitySaveInstanceState:${System.currentTimeMillis()}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.e(activity.javaClass.simpleName, "onActivityDestroyed:${System.currentTimeMillis()}")
            }
        })
    }
}