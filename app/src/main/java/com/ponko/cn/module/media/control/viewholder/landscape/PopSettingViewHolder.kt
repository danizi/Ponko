package com.ponko.cn.module.media.control.viewholder.landscape

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.xm.lib.media.R
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.view.XmPopWindow

/**
 * 弹出框设置
 */
class PopSettingViewHolder private constructor(val xmPopWindow: XmPopWindow, val xmVideoView: XmVideoView?, val context: Context, val tvEdit: TextView, val clAction: ConstraintLayout, val imageView: ImageView, val clSpeed: ConstraintLayout, val tvSpeedTitle: TextView, val tvSpeed05: TextView, val tvSpeed075: TextView, val tvSpeed10: TextView, val tvSpeed125: TextView, val tvSpeed15: TextView, val tvSpeed20: TextView, val clTimerTop: ConstraintLayout, val tvTimerTitle: TextView, val tvTimerNoOpen: TextView, val tvTimerComplete: TextView, val tvTimerCustom: TextView, val clPlayType: ConstraintLayout, val tvPlayTypeTitle: TextView, val tvPlayTypeAuto: TextView, val tvPlayTypeListLoop: TextView, val tvPlayTypeLoop: TextView, val tvPlayTypeCustom: TextView, val clCanvas: ConstraintLayout, val tvCanvasTitle: TextView, val tvCanvasFit: TextView, val tvCanvasFill: TextView, val tvCanvas169: TextView, val tvCanvas43: TextView) {
    companion object {

        fun create(xmVideoView: XmVideoView?, rootView: View?, xmPopWindow: XmPopWindow): PopSettingViewHolder {
            val tvEdit = rootView?.findViewById<View>(R.id.tv_edit) as TextView
            val clAction = rootView.findViewById<View>(R.id.cl_action) as ConstraintLayout
            val imageView = rootView.findViewById<View>(R.id.imageView) as ImageView
            val clSpeed = rootView.findViewById<View>(R.id.cl_speed) as ConstraintLayout
            val tvSpeedTitle = rootView.findViewById<View>(R.id.tv_speed_title) as TextView
            val tvSpeed05 = rootView.findViewById<View>(R.id.tv_speed_05) as TextView
            val tvSpeed075 = rootView.findViewById<View>(R.id.tv_speed_075) as TextView
            val tvSpeed10 = rootView.findViewById<View>(R.id.tv_speed_10) as TextView
            val tvSpeed125 = rootView.findViewById<View>(R.id.tv_speed_125) as TextView
            val tvSpeed15 = rootView.findViewById<View>(R.id.tv_speed_15) as TextView
            val tvSpeed20 = rootView.findViewById<View>(R.id.tv_speed_20) as TextView
            val clTimerTop = rootView.findViewById<View>(R.id.cl_timer_top) as ConstraintLayout
            val tvTimerTitle = rootView.findViewById<View>(R.id.tv_timer_title) as TextView
            val tvTimerNoOpen = rootView.findViewById<View>(R.id.tv_timer_no_open) as TextView
            val tvTimerComplete = rootView.findViewById<View>(R.id.tv_timer_complete) as TextView
            val tvTimerCustom = rootView.findViewById<View>(R.id.tv_timer_custom) as TextView
            val clPlayType = rootView.findViewById<View>(R.id.cl_play_type) as ConstraintLayout
            val tvPlayTypeTitle = rootView.findViewById<View>(R.id.tv_play_type_title) as TextView
            val tvPlayTypeAuto = rootView.findViewById<View>(R.id.tv_play_type_auto) as TextView
            val tvPlayTypeListLoop = rootView.findViewById<View>(R.id.tv_play_type_list_loop) as TextView
            val tvPlayTypeLoop = rootView.findViewById<View>(R.id.tv_play_type_loop) as TextView
            val tvPlayTypeCustom = rootView.findViewById<View>(R.id.tv_play_type_custom) as TextView
            val clCanvas = rootView.findViewById<View>(R.id.cl_canvas) as ConstraintLayout
            val tvCanvasTitle = rootView.findViewById<View>(R.id.tv_canvas_title) as TextView
            val tvCanvasFit = rootView.findViewById<View>(R.id.tv_canvas_fit) as TextView
            val tvCanvasFill = rootView.findViewById<View>(R.id.tv_canvas_fill) as TextView
            val tvCanvas169 = rootView.findViewById<View>(R.id.tv_canvas_16_9) as TextView
            val tvCanvas43 = rootView.findViewById<View>(R.id.tv_canvas_4_3) as TextView
            return PopSettingViewHolder(xmPopWindow, xmVideoView, rootView.context, tvEdit, clAction, imageView, clSpeed, tvSpeedTitle, tvSpeed05, tvSpeed075, tvSpeed10, tvSpeed125, tvSpeed15, tvSpeed20, clTimerTop, tvTimerTitle, tvTimerNoOpen, tvTimerComplete, tvTimerCustom, clPlayType, tvPlayTypeTitle, tvPlayTypeAuto, tvPlayTypeListLoop, tvPlayTypeLoop, tvPlayTypeCustom, clCanvas, tvCanvasTitle, tvCanvasFit, tvCanvasFill, tvCanvas169, tvCanvas43)
        }
    }

    init {
        tvSpeed05.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(0.5f)
            Toast.makeText(context, "0.5倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
        tvSpeed075.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(0.75f)
            Toast.makeText(context, "0.75倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
        tvSpeed10.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(1.0f)
            Toast.makeText(context, "1.0倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
        tvSpeed125.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(1.25f)
            Toast.makeText(context, "1.25倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
        tvSpeed15.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(1.5f)
            Toast.makeText(context, "1.5倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
        tvSpeed20.setOnClickListener {
            xmVideoView?.mediaPlayer?.setSpeed(2.0f)
            Toast.makeText(context, "2.0倍速度", Toast.LENGTH_SHORT).show()
            dismiss(xmPopWindow)
        }
    }

    fun dismiss(xmPopWindow: XmPopWindow) {
        if (xmPopWindow.isShowing) {
            xmPopWindow.dismiss()
        }
    }
}
