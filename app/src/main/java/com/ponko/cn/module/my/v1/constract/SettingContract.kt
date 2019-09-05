package com.ponko.cn.module.my.constract

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.utils.CacheUtil.getLog
import com.ponko.cn.utils.CacheUtil.getMediaBackground
import com.ponko.cn.utils.CacheUtil.getMediaType
import com.ponko.cn.utils.CacheUtil.getRunqueues
import com.ponko.cn.utils.CacheUtil.getStudyUI
import com.ponko.cn.utils.CacheUtil.putLog
import com.ponko.cn.utils.CacheUtil.putMediaBackground
import com.ponko.cn.utils.CacheUtil.putMediaType
import com.ponko.cn.utils.CacheUtil.putRunqueues
import com.ponko.cn.utils.CacheUtil.putStudyUI
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.IntoTargetUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
import java.io.File


/**
 * 设置契约类 - MVP模式
 */
class SettingContract {
    /**
     * 视图层
     */
    interface V {

        class SettingItemViewHolder(view: View) : BaseViewHolder(view) {

            private var ui: UI? = null
            override fun bindData(d: Any, position: Int) {
                if (ui == null) {
                    ui = UI.create(itemView)
                }
                val context = itemView.context
                val settingDes = d as String
                ui?.tv?.text = settingDes
                val list = ArrayList<String>()
                when (settingDes) {
                    //选定分辨率就会下载指定的视频
                    "视频分辨率" -> {
                        list.add("流畅")
                        list.add("标清")
                        list.add("高清")
                        //选中第几个
                        val pos = getMediaType().toInt() - 1
                        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                        ui?.spinner?.adapter = adapter
                        adapter.notifyDataSetChanged()
                        ui?.spinner?.setSelection(pos)
                        ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                //ToastUtil.show("pos:$position")
                                putMediaType((position + 1).toString())
                            }
                        }
                    }
                    "日志开关" -> {
                        log(context, list)
                    }
                    "学习页面新旧版开关" -> {
                        testStudyPage(context, list)
                    }
                    "UI调试开关" -> {
                        testUI(context, list)
                    }
                    "跳转测试开关" -> {
                        testJump(context, list)
                    }
                    "下载任务数量" -> {
                        list.add("3")
                        list.add("4")
                        list.add("5")
                        //选中第几个
                        val pos = getRunqueues().toInt()
                        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                        ui?.spinner?.adapter = adapter
                        adapter.notifyDataSetChanged()
                        ui?.spinner?.setSelection(pos)
                        ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                //ToastUtil.show("pos:$position")
                                when (position) {
                                    0 -> {
                                        PonkoApp.m3u8DownManager?.dispatcher?.runqueues = 3
                                    }
                                    1 -> {
                                        PonkoApp.m3u8DownManager?.dispatcher?.runqueues = 4
                                    }
                                    2 -> {
                                        PonkoApp.m3u8DownManager?.dispatcher?.runqueues = 5
                                    }
                                }
                                putRunqueues(position)
                            }
                        }

                    }
                    "清除视频缓存" -> {
                        ui?.spinner?.visibility = View.INVISIBLE
                        itemView.setOnClickListener {
                            DialogUtil.show(context, "", "确定删除视频缓存？", false, object : OnEnterListener {
                                override fun onEnter(dlg: AlertDialog) {
                                    dlg.dismiss()
                                    val file = File(PonkoApp.m3u8DownManager?.path + File.separator + PonkoApp.m3u8DownManager?.dir)
                                    if (file.exists()) {
                                        if (FileUtil.delAll(file)) {
                                            ToastUtil.show("删除视频缓存成功")
                                        }
                                    } else {
                                        ToastUtil.show("视频未缓存，缓存目录不存在。")
                                    }
                                    PonkoApp.m3u8DownManager?.dao?.deleteAll()
                                    PonkoApp.courseDao?.deleteAll()
                                    PonkoApp.courseSpecialDao?.deleteAll()

                                    val file2 = File(PonkoApp.m3u8DownManager?.path + File.separator + "ponkoDownload")
                                    val file3 = File(PonkoApp.m3u8DownManager?.path + File.separator + "polyvdownload")
                                    if (file2.exists()) {
                                        if (FileUtil.delAll(file2)) {
                                            ToastUtil.show("删除ponkoDownload缓存目录成功")
                                        }
                                    }
                                    if (file3.exists()) {
                                        if (FileUtil.delAll(file3)) {
                                            ToastUtil.show("删除[polyvdownload]缓存目录成功")
                                        }
                                    }
                                }
                            }, object : OnCancelListener {
                                override fun onCancel(dlg: AlertDialog) {
                                    dlg.dismiss()
                                }
                            })
                        }
                    }
                    "播放器是否后台播放" -> {
                        list.add("是")
                        list.add("否")
                        //选中第几个
                        val pos = getMediaBackground().toInt() - 1
                        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                        ui?.spinner?.adapter = adapter
                        adapter.notifyDataSetChanged()
                        ui?.spinner?.setSelection(pos)
                        ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                putMediaBackground(position.toString())
                            }
                        }
                    }
                    else -> {

                    }
                }
            }

            private fun log(context: Context, list: ArrayList<String>) {
                list.add("打开")
                list.add("关闭")
                val pos = getLog().toInt() - 1
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                ui?.spinner?.adapter = adapter
                adapter.notifyDataSetChanged()
                ui?.spinner?.setSelection(pos)
                ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //ToastUtil.show("pos:$position")
                        when (position) {
                            0 -> {
                                BKLog.LEVEL = BKLog.D
                                putLog("1")
                            }
                            1 -> {
                                BKLog.LEVEL = BKLog.E
                                putLog("2")
                            }
                        }
                    }
                }
            }

            private fun testStudyPage(context: Context, list: ArrayList<String>) {
                list.add("旧版")
                list.add("新版")
                val pos = getStudyUI().toInt() - 1
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                ui?.spinner?.adapter = adapter
                adapter.notifyDataSetChanged()
                ui?.spinner?.setSelection(pos)
                ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //ToastUtil.show("pos:$position")
                        when (position) {
                            0 -> {
                                BKLog.LEVEL = BKLog.D
                                putStudyUI("1")
                            }
                            1 -> {
                                BKLog.LEVEL = BKLog.E
                                putStudyUI("2")
                            }
                        }
                    }
                }
            }

            private fun testUI(context: Context, list: ArrayList<String>) {
                list.add("打开")
                list.add("关闭")
                val pos = getStudyUI().toInt() - 1
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                ui?.spinner?.adapter = adapter
                adapter.notifyDataSetChanged()
                ui?.spinner?.setSelection(pos)
                ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //ToastUtil.show("pos:$position")
                        when (position) {
                            0 -> {
                                BKLog.LEVEL = BKLog.D
                                putStudyUI("1")
                            }
                            1 -> {
                                BKLog.LEVEL = BKLog.E
                                putStudyUI("2")
                            }
                        }
                    }
                }
            }

            private fun testJump(context: Context, list: ArrayList<String>) {
                list.add("关闭")
                list.add("微信绑定操作")
                list.add("交流页面")
                list.add("免费页面")
                list.add("积分产品详情页面")
                list.add("积分排行榜")
                list.add("积分阅换记录")
                list.add("积分获取记录")
                list.add("积分任务")
                list.add("积分商城")
                list.add("课程详情")
                list.add("支付网页")
                list.add("普通网页")
                list.add("提醒")
                list.add("邀请")
                list.add("个人信息编辑页面")
                val pos = getStudyUI().toInt() - 1
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, list)
                ui?.spinner?.adapter = adapter
                adapter.notifyDataSetChanged()
                ui?.spinner?.setSelection(pos)
                ui?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //ToastUtil.show("pos:$position")
                        when (position) {
                            0 -> {
                            }
                            1 -> {
                                IntoTargetUtil.target(context, "wechat_bind", "")
                            }
                            2 -> {
                                IntoTargetUtil.target(context, "case"/*"交流页面"*/, "2")
                            }
                            3 -> {
                                IntoTargetUtil.target(context, "free"/*"免费页面"*/, "1")
                            }
                            4 -> {
                                IntoTargetUtil.target(context, "store_product_detail"/*"积分产品详情页面"*/, "/store/exchange_detail?id=fc4b7d755cdf11e88b440242ac130003")
                            }
                            5 -> {
                                IntoTargetUtil.target(context, "store_score_ranking"/*"积分排行榜"*/, "")
                            }
                            6 -> {
                                IntoTargetUtil.target(context, "store_score_exchange_list"/*"积分阅换记录"*/, "")
                            }
                            7 -> {
                                IntoTargetUtil.target(context, "store_score_list"/*"积分获取记录"*/, "")
                            }
                            8 -> {
                                //一般不会使用
                                IntoTargetUtil.target(context, "store_score_task"/*"积分任务"*/, "xxxx")
                            }
                            9 -> {
                                IntoTargetUtil.target(context, "store"/*"积分商城"*/, "")
                            }
                            10 -> {
                                IntoTargetUtil.target(context, "course_detail"/*"课程详情"*/, "f54bcf82547211e69a5500163e000c35")
                            }
                            11 -> {
                                IntoTargetUtil.target(context, "payment"/*"支付网页"*/, "/product/detail?productId=bfd7f85c6f0a11e8a93a67b75eb469f2")
                            }
                            12 -> {
                                IntoTargetUtil.target(context, "url"/*"普通网页"*/, "analysis/detail?id=" + "2674ebd90b3a11e9b5c00242ac130004")
                            }
                            13 -> {
                                IntoTargetUtil.target(context, "msg"/*"提醒"*/, "")
                            }
                            14 -> {
                                IntoTargetUtil.target(context, "invite"/*"邀请"*/, "")
                            }
                            15 -> {
                                IntoTargetUtil.target(context, "profile_edit"/*"个人信息编辑页面"*/, "")
                            }
                        }
                    }
                }
            }

            private class UI private constructor(val spinner: AppCompatSpinner, val tv: TextView) {
                companion object {

                    fun create(rootView: View): UI {
                        val spinner = rootView.findViewById<View>(R.id.spinner) as AppCompatSpinner
                        val tv = rootView.findViewById<View>(R.id.tv) as TextView
                        return UI(spinner, tv)
                    }
                }
            }
        }
    }

    /**
     * 数据层
     */
    class M {

    }

    /**
     * 控制层
     */
    class Present {

    }
}