package com.wk.mycustomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.wk.mycustomview.widget.AvatarView
import com.wk.mycustomview.widget.PieView

class MainActivity : AppCompatActivity() {
    private var id = R.drawable.bg_rain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pieView = findViewById<AvatarView>(R.id.pieView)
        findViewById<TextView>(R.id.tvSwitchPie).setOnClickListener {
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