package com.wk.mycustomview.widget.animate

import android.view.View

object AnimateUtil {

    fun startAnimate(view: View) {
        view.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .alpha(0.5f)
            .translationX(100f)
            .translationY(100f)
            .rotation(90f)
            .setStartDelay(3000)
            .setDuration(3000)
            .start()
    }
}