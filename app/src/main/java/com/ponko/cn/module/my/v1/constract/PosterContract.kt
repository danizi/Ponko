package com.ponko.cn.module.my.v1.constract

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.ReportShareCBean
import com.ponko.cn.http.HttpCallBack
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.NumUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.common.util.ViewUtil
import com.xm.lib.component.OnEnterListener
import com.xm.lib.downloader.utils.CommonUtil.saveBmp2Gallery
import retrofit2.Call
import retrofit2.Response


/**
 * 海报分享契约类
 */
class PosterContract {

    interface V {
        /**
         * 设置当前日期
         */
        fun setCurrentDate(year: String, mmdd: String, whatDay: String)

        /**
         * 海报背景
         */
        fun setPosterBk(bg: String?)

        /**
         * 鸡汤语
         */
        fun setPosterChickenWord(title: String?)

        /**
         * 设置头像和昵称
         */
        fun setPersonInfo(data: ReportShareCBean.DataBean?)

        /**
         * 设置二维码信息
         */
        fun setQRInfo(qr: ReportShareCBean.QrBean?)

        /**
         * 设置打卡信息记录
         */
        fun setRecordInfo(count: ReportShareCBean.CountBeanX?)

        /**
         * 设置分享按钮提示语
         */
        fun setShareBtn(btn: String?)

        /**
         * 获取海报布局 bmp
         */
        fun getShareBmp(): Bitmap

        /**
         * 请求接口成功
         */
        fun requestReportShareApiSuccess(body: ReportShareCBean?)

        /**
         * 请求接口失败
         */
        fun requestReportShareApiFailure(msg: String?)

        fun setTitle(title: String?)

    }

    class M {
        /**
         * 请求获取学习海报信息
         */
        fun requestReportShare(callback: HttpCallBack<ReportShareCBean>?) {
            if (callback != null) {
                PonkoApp.myApi?.reportShare()?.enqueue(callback)
            } else {
                BKLog.e("requestReportShare callback is null!!!")
            }
        }
    }

    class P(private val ctx: Context?, private val v: V?) {
        private val m = M()

        /**
         * 获取当前时间
         */
        fun getCurrentDate() {
            val year = TimeUtil.getYear().toString()
            val mmdd = NumUtil.getDecimalPoint(TimeUtil.getMonth(), "00") + " - " + NumUtil.getDecimalPoint(TimeUtil.getDay(), "00")
            val whatDay = TimeUtil.getWeek()
            v?.setCurrentDate(year, mmdd, whatDay)
        }

        /**
         * 请求海报信息接口
         */
        fun requestReportShareApi() {
            m.requestReportShare(object : HttpCallBack<ReportShareCBean>() {
                override fun onSuccess(call: Call<ReportShareCBean>?, response: Response<ReportShareCBean>?) {
                    val body = response?.body()
                    //设置标题栏
                    v?.setTitle(body?.title)
                    //海报背景
                    v?.setPosterBk(body?.bg)
                    //鸡汤语
                    v?.setPosterChickenWord(body?.subtitle)
                    //设置头像和昵称
                    v?.setPersonInfo(body?.data)
                    //设置二维码信息
                    v?.setQRInfo(body?.qr)
                    //设置打卡信息记录
                    v?.setRecordInfo(body?.count)
                    //设置分享按钮提示语
                    v?.setShareBtn(body?.btn)
                    //请求接口成功
                    v?.requestReportShareApiSuccess(body)
                }

                override fun onFailure(call: Call<ReportShareCBean>?, msg: String?) {
                    super.onFailure(call, msg)
                    v?.requestReportShareApiFailure(msg)
                    BKLog.e("requestReportShareApi is failure msg:$msg")
                }
            })
        }

        /**
         * 点击分享按钮
         */
        fun clickShareBtn() {
            val bmp = v?.getShareBmp()
            if (bmp != null) {
                DialogUtil.showShareImg(ctx, ViewUtil.setBmp(bmp))
            } else {
                ToastUtil.show("分享海报失败,bitmap is null!!!")
            }
        }

        /**
         * 刷新处理
         */
        fun refresh() {
            requestReportShareApi()
        }

        /**
         * 保存海报到本地相册
         */
        fun savePoster() {
            DialogUtil.show(ctx!!, "提示", " 海报是否保存到本地？", true, object : OnEnterListener {
                override fun onEnter(dlg: AlertDialog) {
                    saveBmp2Gallery(ctx, v?.getShareBmp()!!, "poster")
                    dlg.dismiss()
                }
            }, null)
        }
    }
}