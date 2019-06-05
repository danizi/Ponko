package com.ponko.cn.module.my.constract

import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.utils.CacheUtil.getLog
import com.ponko.cn.utils.CacheUtil.getMediaType
import com.ponko.cn.utils.CacheUtil.getStudyUI
import com.ponko.cn.utils.CacheUtil.putLog
import com.ponko.cn.utils.CacheUtil.putMediaType
import com.ponko.cn.utils.CacheUtil.putStudyUI
import com.xm.lib.common.base.rv.BaseViewHolder
import com.xm.lib.common.log.BKLog
import kotlin.math.log


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
                                //关闭
                            }
                            1 -> {

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