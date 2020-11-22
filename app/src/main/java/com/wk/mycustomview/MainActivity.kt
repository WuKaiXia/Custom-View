package com.wk.mycustomview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wk.mycustomview.widget.AvatarView
import com.wk.mycustomview.widget.PieView
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
    }
}