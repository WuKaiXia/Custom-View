package com.wk.mycustomview

import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wk.mycustomview.widget.AvatarView
import com.wk.mycustomview.widget.PieView
import com.wk.mycustomview.widget.xfermode.XfermodeView

class XfermodeActivity : AppCompatActivity() {
    private val data = arrayListOf<XfermodeBean>().apply {
        add(XfermodeBean("CLEAR", PorterDuff.Mode.CLEAR))
        add(XfermodeBean("SRC", PorterDuff.Mode.SRC))
        add(XfermodeBean("DST", PorterDuff.Mode.DST))
        add(XfermodeBean("SRC_OVER", PorterDuff.Mode.SRC_OVER))
        add(XfermodeBean("DST_OVER", PorterDuff.Mode.DST_OVER))
        add(XfermodeBean("SRC_IN", PorterDuff.Mode.SRC_IN))
        add(XfermodeBean("DST_IN", PorterDuff.Mode.DST_IN))
        add(XfermodeBean("SRC_OUT", PorterDuff.Mode.SRC_OUT))
        add(XfermodeBean("DST_OUT", PorterDuff.Mode.DST_OUT))
        add(XfermodeBean("SRC_ATOP", PorterDuff.Mode.SRC_ATOP))
        add(XfermodeBean("DST_ATOP", PorterDuff.Mode.DST_ATOP))
        add(XfermodeBean("XOR", PorterDuff.Mode.XOR))
        add(XfermodeBean("DARKEN", PorterDuff.Mode.DARKEN))
        add(XfermodeBean("LIGHTEN", PorterDuff.Mode.LIGHTEN))
        add(XfermodeBean("MULTIPLY", PorterDuff.Mode.MULTIPLY))
        add(XfermodeBean("SCREEN", PorterDuff.Mode.SCREEN))
        add(XfermodeBean("ADD", PorterDuff.Mode.ADD))
        add(XfermodeBean("OVERLAY", PorterDuff.Mode.OVERLAY))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        val rvList = findViewById<RecyclerView>(R.id.rvList)
        rvList.adapter = MyAdapter(this, data)
    }
}

class MyAdapter(val mContext: Context, val data: ArrayList<XfermodeBean>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setText(id: Int, text: String) {
            itemView.findViewById<TextView>(id).text = text
        }

        fun setMode(id: Int, mode: PorterDuff.Mode): MyViewHolder {
            itemView.findViewById<XfermodeView>(id).mode = mode
            return this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_xfermode, null, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.setMode(R.id.xv, item.modeType)
            .setText(R.id.tvTitle, item.modeName)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

data class XfermodeBean(val modeName: String, val modeType: PorterDuff.Mode)