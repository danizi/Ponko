package com.ponko.cn.module.media

import com.google.gson.Gson
import com.ponko.cn.bean.VideoInfoCBean
import okhttp3.*
import java.io.IOException

object MediaUitl {
    private const val TAG = "MediaUitl"
    /**
     * 获取播放地址
     * @param vid        视频唯一标识         26de49f8c22abafd8adc1b49246262c6_2
     * @param userid     保利威视提供唯一标识 26de49f8c2
     * @param secretKey  保利威视提供的key    ETd98zg5Ka
     */
    fun getUrlByVid(vid: String?, userid: String, secretKey: String, listener: OnVideoInfoListener?) {
        val url = "https://api.polyv.net/v2/video/$userid/get-video-msg"
        val format = "json"
        val ptime = java.lang.Long.toString(System.currentTimeMillis())
        val sign = SHA1Util.hexString(SHA1Util.eccryptSHA1("format=$format&ptime=$ptime&vid=$vid$secretKey")).toUpperCase()
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
                //println( "保利威视视频详细信息:" + response.body()!!.string())
                listener?.onSuccess(Gson().fromJson(response.body()!!.string(), VideoInfoCBean::class.java))
            }
        })
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