package com.ponko.cn.module.study2.holder

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.bean.Main2CBean
import com.ponko.cn.bean.StudyHomePayBean
import com.ponko.cn.bean.StudyHomeUnPayBean
import com.ponko.cn.utils.Glide
import com.ponko.cn.utils.IntoTargetUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.util.ScreenUtil

/**
 * 未支付课程列表
 */
class UnPayViewHolder(view: View) : BaseViewHolder(view) {

    private var ui: UI? = null

    override fun bindData(d: Any, position: Int) {
        if (ui == null) {
            ui = UI.create(itemView)
        }
        val context = itemView.context
        val studyHomeUnPayBean = d as StudyHomeUnPayBean
        ui?.tvTitle?.text = "未入学"
        val adapter = object : BaseRvAdapter() {}
        adapter.addItemViewDelegate(0, ItemViewHolder::class.java, Any::class.java, R.layout.item_study_home_course)
        adapter.data?.addAll(studyHomeUnPayBean.productsAll)
        ui?.rv?.adapter = adapter
        if (ScreenUtil.isPad(context)) {
            ui?.rv?.layoutManager = GridLayoutManager(context, 2)
        } else {
            ui?.rv?.layoutManager = LinearLayoutManager(context)
        }
        ui?.rv?.isFocusableInTouchMode = false
    }

    private class UI private constructor(val tvTitle: TextView, val rv: RecyclerView) {
        companion object {

            fun create(rootView: View): UI {
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
                return UI(tvTitle, rv)
            }
        }
    }

    class ItemViewHolder(view: View) : BaseViewHolder(view) {
        private var ui: UI? = null
        override fun bindData(d: Any, position: Int) {
            if (ui == null) {
                ui = UI.create(itemView)
            }
            val context = itemView.context
            val productsAllBean = d as Main2CBean.ProductsAllBean
            Glide.with(context, productsAllBean.avatar, ui?.ivCourse)
            //图片高度自适应
            val present = 155f / 347f
            val layoutParams = ui?.ivCourse?.layoutParams
            layoutParams?.height = ((ScreenUtil.getNormalWH(context as Activity)[0] - ScreenUtil.dip2px(context, 30)) * present).toInt()
            ui?.ivCourse?.layoutParams = layoutParams
            itemView.setOnClickListener {
                IntoTargetUtil.target(context, productsAllBean.link_type, productsAllBean.link_value)
            }
        }

        private class UI private constructor(val ivCourse: ImageView) {
            companion object {

                fun create(rootView: View): UI {
                    val ivCourse = rootView.findViewById<View>(R.id.iv_course) as ImageView
                    return UI(ivCourse)
                }
            }
        }
    }
}