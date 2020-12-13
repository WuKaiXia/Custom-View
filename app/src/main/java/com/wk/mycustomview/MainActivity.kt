package com.wk.mycustomview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.wk.mycustomview.widget.AvatarView
import com.wk.mycustomview.widget.PieView
import com.wk.mycustomview.widget.animate.AnimateUtil
import com.wk.mycustomview.widget.animate.CircleView
import com.wk.mycustomview.widget.canvas.CanvasView

class MainActivity : AppCompatActivity() {
    private var id = R.drawable.bg_rain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<CanvasView>(R.id.cvView).run {
            isCamera = true
            val animator = ObjectAnimator.ofFloat(this, "flipRotation", 360f)

            val topAnimator = ObjectAnimator.ofFloat(this, "topFlip", -60f)

            val bottomAnimator = ObjectAnimator.ofFloat(this, "bottomFlip", 60f)

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(bottomAnimator, animator, topAnimator)
            animatorSet.startDelay = 2000
            animatorSet.duration = 2000
            animatorSet.start()
        }
        val pieView = findViewById<AvatarView>(R.id.pieView)
        findViewById<TextView>(R.id.tvSwitchPie).setOnClickListener {
            startActivity(Intent(this, XfermodeActivity::class.java))
//            pieView.switchPie()
            id = if (id == R.drawable.bg_rain) {
                R.drawable.test
            } else {
                R.drawable.bg_rain
            }
            pieView.setSrc(id)
        }

        AnimateUtil.startAnimate(pieView)
        pieView.setOnClickListener {
            Log.e(this::class.java.simpleName, "pie View")
        }

        animatorTest()
    }

    fun animatorTest() {
        val cvAnimator = findViewById<CircleView>(R.id.cvAnimator)
        cvAnimator.startAnimation(150f.px, Color.BLUE)
    }
}