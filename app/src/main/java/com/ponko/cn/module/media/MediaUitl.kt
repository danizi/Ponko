package com.ponko.cn.module.media

import android.text.TextUtils
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MainCBean
import com.ponko.cn.bean.VideoInfoCBean
import com.ponko.cn.utils.CacheUtil.getPolycConfig
import com.xm.lib.common.log.BKLog
import okhttp3.*
import java.io.IOException


object MediaUitl {
    private const val TAG = "MediaUitl"
    /**
     * 三种选择 1 标准  2 高清 3超高清
     */
    private const val QUALITY = 1

    /**
     * 视频地址选择 ps:调用接口有时候地址是空的
     */
    fun hls(section: List<VideoInfoCBean.DataBean>?): String {
        if (section?.isEmpty()!!) {
            throw NullPointerException("请检查保利威视接口返回的视频播放地址信息")
        }
        return when (QUALITY) {
            1 -> section[0].hls[0]
            2 -> section[0].hls[1]
            3 -> section[0].hls[2]
            else -> {
                section[0].hls[2]
            }
        }
    }

    /**
     * 视频大小选择 ps:调用接口是不会为空的
     */
    fun fileSize(section: List<VideoInfoCBean.DataBean>?): Int {
        if (section?.isEmpty()!!) {
            throw NullPointerException("请检查保利威视接口返回的视频大小信息")
        }
        return when (QUALITY) {
            1 -> section[0].filesize[0]
            2 -> section[0].filesize[1]
            3 -> section[0].filesize[2]
            else -> {
                section[0].filesize[2]
            }
        }
    }

    /**
     * 视频大小选择
     */
    fun fileSize(section: CoursesDetailCBean.ChaptersBean.SectionsBean?): Int {
        if (section == null) {
            throw NullPointerException("请检查保利威视接口返回的视频大小信息")
        }
        return when (QUALITY) {
            1 -> section.filesize1
            2 -> section.filesize2
            3 -> section.filesize3
            else -> {
                section.filesize3
            }
        }
    }

    /**
     * 通过vid获取视频信息
     * @param vid        视频唯一标识         26de49f8c22abafd8adc1b49246262c6_2
     * @param userid     保利威视提供唯一标识 26de49f8c2
     * @param secretKey  保利威视提供的key    ETd98zg5Ka
     */
    @Deprecated("最好是使用下面的")
    fun getUrlByVid(vid: String?, userid: String, secretKey: String, listener: OnVideoInfoListener?) {
        var id = userid
        var key = secretKey
        val polyvBean = Gson().fromJson(getPolycConfig(), MainCBean.PolyvBean::class.java)
        if (TextUtils.isEmpty(userid)) {
            id = polyvBean.user_id
        }
        if (TextUtils.isEmpty(secretKey)) {
            key = polyvBean.secret_key
        }

        val url = "https://api.polyv.net/v2/video/$id/get-video-msg"
        val format = "json"
        val ptime = java.lang.Long.toString(System.currentTimeMillis())
        val sign = SHA1Util.hexString(SHA1Util.eccryptSHA1("format=$format&ptime=$ptime&vid=$vid$key")).toUpperCase()
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
                .url("$url?format=$format&ptime=$ptime&vid=$vid&sign=$sign")
                .post(RequestBody.create(null, "")) //传递一个nullrequestBody
                .build()
        client.newCall(request)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                listener?.onFailure()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //BKLog.d(TAG, "保利威视视频详细信息:" + response.body()!!.string())
                val tempResponse = response.body()?.string()
                //println("保利威视视频详细信息:$tempResponse")
                try {
                    listener?.onSuccess(Gson().fromJson(tempResponse, VideoInfoCBean::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    /**
     * 通过vid获取视频信息
     */
    fun getUrlByVid(vid: String?, listener: OnPlayUrlListener?) {
        //获取保利威视的视频配置信息 ps:在请求首页接口时就保存了
        val polyvBean = Gson().fromJson(getPolycConfig(), MainCBean.PolyvBean::class.java)
        getUrlByVid(vid, polyvBean.user_id, polyvBean.secret_key, object : OnVideoInfoListener {
            override fun onFailure() {
                listener?.onFailure()
                BKLog.e(TAG, "获取网络视频地址失败")
            }

            override fun onSuccess(videoInfo: VideoInfoCBean) {
                //val m3u8 = videoInfo.data[0].hls[2]
                //val total =videoInfo.data[0].filesize[2]
                val m3u8 = hls(videoInfo.data)
                val total = fileSize(videoInfo.data)
                listener?.onSuccess(m3u8, total)
                //存入到本地
                PonkoApp.courseDao?.requestVideoInfoUpdate(vid, m3u8, total)
                BKLog.d(TAG, "播放网络视频地址 : $m3u8 ")
            }
        })
    }

    /**
     * 获取播放地址，第一种是网络m3u8地址，另一种缓存m3u8
     * @param vid      视频唯一标识
     * @param listener 播放地址获取监听
     */
    fun getM3u8Url(vid: String?, listener: OnPlayUrlListener?) {
        //从数据库中查询 指定vid的课程信息
        val courses = PonkoApp.courseDao?.select(vid)
        if (courses?.size!! > 0) {
            //如果有m3u8视频缓存路径，则使用本地播放
            val cacheM3u8 = courses[0].column_down_path
            val total = courses[0].column_total
            if (!TextUtils.isEmpty(cacheM3u8)) {
                listener?.onSuccess(cacheM3u8, total)
                BKLog.d(TAG, "播放缓存视频地址 : $cacheM3u8 ")
            } else {
                //播放m3u8网络地址
                getUrlByVid(vid, listener)
            }
        } else {
            getUrlByVid(vid, listener)
        }
    }

    /**
     * 获取播放地址监听
     */
    interface OnPlayUrlListener {
        fun onFailure()
        fun onSuccess(url: String, size: Int? = 0)
    }

    /**
     * 获取视频信息监听
     */
    interface OnVideoInfoListener {
        fun onFailure()
        fun onSuccess(videoInfo: VideoInfoCBean)
    }

    /**
     * 测试
     */
    @JvmStatic
    fun main(args: Array<String>) {
        getUrlByVid("26de49f8c22abafd8adc1b49246262c6_2", "26de49f8c2", "ETd98zg5Ka", null)
    }
}