package com.ponko.cn.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.xm.lib.common.util.ViewUtil

object BarUtil {

    /**
     * 学习首页搜索顶部
     */
    fun addSearchBar(context: Context?, toolbar: Toolbar?, searchListener: View.OnClickListener, historyListener: View.OnClickListener, helpListener: View.OnClickListener) {
        val barView = ViewUtil.viewById(context, R.layout.bar_search, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val tvSearch: TextView? = barView?.findViewById(R.id.tv_search) as TextView
        val ivHistory: ImageView? = barView.findViewById(R.id.iv_history) as ImageView
        val ivHelp: ImageView? = barView.findViewById(R.id.iv_help) as ImageView
        tvSearch?.setOnClickListener(searchListener)
        ivHistory?.setOnClickListener(historyListener)
        ivHelp?.setOnClickListener(helpListener)
    }

    /**
     * 搜索界面顶部
     */
    fun addSearchBar2(context: Context?, toolbar: Toolbar?, right: String? = "取消", rightListener: View.OnClickListener?, watcher: TextWatcher?, editorActionListener: TextView.OnEditorActionListener?) {
        val barView = ViewUtil.viewById(context, R.layout.bar_search2, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val tvSearch = barView?.findViewById(R.id.tv_search) as EditText
        val tvCancel = barView.findViewById(R.id.tv_cancel) as TextView
        tvCancel.text = right
        tvCancel.setOnClickListener(rightListener)
        tvSearch.addTextChangedListener(watcher)
        tvSearch.setOnEditorActionListener(editorActionListener)
    }

    fun addWhiteBar(context: Context?, toolbar: Toolbar?, title: String, barRight: String? = "", barRightlistener: View.OnClickListener? = null) {
        val barView = ViewUtil.viewById(context, R.layout.bar_white, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        if (barRightlistener != null) {
            tvBarRight?.setOnClickListener(barRightlistener)
        }
        tvTitle?.text = title
        tvBarRight?.text = barRight
    }

    fun addBar1(context: Context?, toolbar: Toolbar?, title: String, right: String? = "", listener: View.OnClickListener? = null) {
        val barView = ViewUtil.viewById(context, R.layout.bar_1, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
        val tvRight: TextView? = barView.findViewById(R.id.tv_right) as TextView

        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        tvRight?.setOnClickListener(listener)
        tvTitle?.text = title
    }

    fun addBar2(context: Context?, toolbar: Toolbar?, title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        val barView = ViewUtil.viewById(context, R.layout.bar_2, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        tvBarRight?.setOnClickListener(barRightlistener)
        tvTitle?.text = title
        tvBarRight?.text = barRight
    }

    fun addBar3(context: Context?, toolbar: Toolbar?, title: String, barRight: String? = "", barRightlistener: View.OnClickListener) {
        val barView = ViewUtil.viewById(context, R.layout.bar_3, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        val tvTitle: TextView? = barView?.findViewById(R.id.tv_title) as TextView
        val tvBarRight: TextView? = barView.findViewById(R.id.tv_bar_right) as TextView
        tvBarRight?.setOnClickListener(barRightlistener)
        tvTitle?.text = title
        tvBarRight?.text = barRight
    }
}