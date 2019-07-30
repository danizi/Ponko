package com.ponko.cn.module.my.v1.constract

import android.content.Context
import android.content.Intent
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ReportCommonBean
import com.ponko.cn.bean.ReportStudyBean
import com.ponko.cn.bean.StudyReportCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.module.my.v1.holder.ReportCommonVH
import com.ponko.cn.module.my.v1.holder.ReportStudyVH
import com.ponko.cn.module.my.v1.option.PosterActivity
import com.ponko.cn.utils.ActivityUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.base.rv.BaseRvAdapter
import retrofit2.Call
import retrofit2.Response

/**
 * 我的学习报告契约类
 */
class ReportContract {
    interface V {
        /**
         * 请求学习报告页面
         */
        fun displayReportPage(body: StudyReportCBean?, rvAdapter: BaseRvAdapter?)

        /**
         * 刷新学习报告页面成功
         */
        fun refreshSuccess(rvAdapter: BaseRvAdapter?)

        /**
         * 刷新学习报告页面失败
         */
        fun refreshFailure()
    }

    class M {
        /**
         * 请求学习报告接口
         */
        fun requestReportApi(callback: HttpCallBack<StudyReportCBean>) {
            PonkoApp.myApi?.studyReport()?.enqueue(callback)
        }
    }

    class P(val context: Context?, val v: V?) {
        /**
         * 数据层
         */
        private val m = M()
        /**
         * 内容组件适配器
         */
        private var rvAdapter: BaseRvAdapter? = null

        /**
         * 请求刷新
         */
        fun refresh() {
            m.requestReportApi(object : HttpCallBack<StudyReportCBean>() {
                override fun onSuccess(call: Call<StudyReportCBean>?, response: Response<StudyReportCBean>?) {
                    //刷新数据操作
                    updateRvData(response?.body())
                }

                private fun updateRvData(body: StudyReportCBean?) {
                    if (rvAdapter?.data?.size!! > 0) {
                        rvAdapter?.data?.clear()
                        for (item in body?.list!!) {
                            if (item.type == "common") {
                                rvAdapter?.data?.add(ReportCommonBean(item.report, item.title, item.list, item.footer))
                            } else if (item.type == "history") {
                                if (!item.list.isEmpty()) {
                                    rvAdapter?.data?.add(ReportStudyBean(item.title, item.list, item.subtitle))
                                }
                            }
                        }
                        v?.refreshSuccess(rvAdapter)
                    }
                }

                override fun onFailure(call: Call<StudyReportCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.refreshFailure()
                }
            })
        }


        /**
         * 请求学习报告接口
         */
        fun requestReportApi() {
            DialogUtil.showProcess(context!!)
            m.requestReportApi(object : HttpCallBack<StudyReportCBean>() {
                override fun onSuccess(call: Call<StudyReportCBean>?, response: Response<StudyReportCBean>?) {
                    //检查数据
                    val body = response?.body()
                    if (body?.list?.isEmpty()!!) {
                        ToastUtil.show("数据为空!!!")
                        DialogUtil.hideProcess()
                        return
                    }

                    //再封装下数据
                    rvAdapter = getAdapter(body)

                    if (rvAdapter == null) {
                        DialogUtil.hideProcess()
                        return
                    }

                    //设置数据页面
                    v?.displayReportPage(response.body(), rvAdapter!!)

                    //隐藏加载框
                    DialogUtil.hideProcess()
                }

                private fun getAdapter(body: StudyReportCBean): BaseRvAdapter? {
                    if (rvAdapter == null) {
                        rvAdapter = object : BaseRvAdapter() {}
                        var viewType = 0
                        for (item in body.list) {
                            if (item.type == "common") {
                                rvAdapter?.data?.add(ReportCommonBean(item.report, item.title, item.list, item.footer))
                                //添加RecyclerView展示类型common
                                rvAdapter?.addItemViewDelegate(viewType, ReportCommonVH::class.java, ReportCommonBean::class.java, R.layout.item_report_title_rv)
                                viewType++
                            } else if (item.type == "history") {
                                if (!item.list.isEmpty()) {
                                    rvAdapter?.data?.add(ReportStudyBean(item.title, item.list, item.subtitle))
                                    //添加RecyclerView展示类型history
                                    rvAdapter?.addItemViewDelegate(viewType, ReportStudyVH::class.java, ReportStudyBean::class.java, R.layout.item_report_title_rv)
                                    viewType++
                                }
                            }
                        }
                        return rvAdapter
                    }
                    return null
                }

                override fun onFailure(call: Call<StudyReportCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    DialogUtil.hideProcess()
                }
            })
        }

        fun intoPosterPage() {
            ActivityUtil.startActivity(context, Intent(context, PosterActivity::class.java))
        }
    }
}