package com.wk.mycustomview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.doOnRepeat
import com.wk.mycustomview.utils.DialogUtils
import com.wk.mycustomview.widget.AvatarView
import com.wk.mycustomview.widget.PieView
import com.wk.mycustomview.widget.animate.AnimateUtil
import com.wk.mycustomview.widget.animate.CircleView
import com.wk.mycustomview.widget.animate.PointFEvaluator
import com.wk.mycustomview.widget.canvas.CanvasView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val dialog by lazy { AlertDialog.Builder(this)
        .setTitle("This is test")
        .setMessage("hello every one")
        .create() }
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
            DialogUtils.getDialogRootView(this)
        }

        AnimateUtil.startAnimate(pieView)
        pieView.setOnClickListener {
            dialog.show()
            DialogUtils.getDialogRootView(this)
            Log.e(this::class.java.simpleName, "pie View")
        }

        animatorTest()

        val evaluator = PointFEvaluator()
        val pointAnimator =
            ObjectAnimator.ofObject(pointF, "pointF", evaluator, PointF(200f.px, 35f.px))
        pointAnimator.run {
            duration = 5000
            repeatMode()
            start()
        }

        provinceText.startAnimation()

        cvAnimator.setOnClickListener {
            startActivity(Intent(this, ActivityViewGroup::class.java))
        }
    }

    fun animatorTest() {
        val cvAnimator = findViewById<CircleView>(R.id.cvAnimator)
        cvAnimator.startAnimation(100f.px, Color.BLUE)
    }
}