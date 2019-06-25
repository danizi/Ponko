package com.ponko.cn.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ponko.cn.R
import com.xm.lib.common.base.ActManager
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

    /**
     * 白色顶部
     */
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

    /**
     * 常规的顶部栏
     */
    fun addBar1(context: Context?, toolbar: Toolbar?, title: String, right: String? = "", listener: View.OnClickListener? = null) {
        if(toolbar?.childCount!!>0){
            toolbar.removeAllViews()
        }
        val barView = ViewUtil.viewById(context, R.layout.bar_1, null)
        toolbar.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar.visibility = View.VISIBLE
        val flBack: FrameLayout? = barView?.findViewById(R.id.fl_back) as FrameLayout
        val tvTitle: TextView? = barView.findViewById(R.id.tv_title) as TextView
        val tvRight: TextView? = barView.findViewById(R.id.tv_right) as TextView
        tvRight?.text = right
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        tvRight?.setOnClickListener(listener)
        tvTitle?.text = title
    }

    /**
     * 顶部栏右边带有图标
     */
    fun addBarIcon(context: Context?, toolbar: Toolbar?, title: String, id: Int/*删除*/, id2: Int/*取消*/, listener: View.OnClickListener) {
        val barView = ViewUtil.viewById(context, R.layout.bar_icon, null)
        toolbar?.addView(barView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        toolbar?.visibility = View.VISIBLE
        var flBack: FrameLayout? = null
        var tvTitle: TextView? = null
        var rlRight: RelativeLayout? = null
        var icon: ImageView? = null
        flBack = barView?.findViewById(R.id.fl_back)
        tvTitle = barView?.findViewById(R.id.tv_title)
        rlRight = barView?.findViewById(R.id.rl_right)
        icon = barView?.findViewById(R.id.icon)
        icon?.setImageResource(id)
        flBack?.setOnClickListener {
            (context as Activity).finish()
        }
        var flag = true
        rlRight?.setOnClickListener { v ->
            listener.onClick(v)
            if (flag) {
                icon?.setImageResource(id2)
            } else {
                icon?.setImageResource(id)
            }
            flag = !flag
        }
        tvTitle?.text = title
    }

    /**
     * 积分商城里面会使用的
     */
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

    /**
     * 一般用于首页四个模块上
     */
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