package com.wk.mycustomview

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
        findViewById<CanvasView>(R.id.cvView).isCamera = true
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