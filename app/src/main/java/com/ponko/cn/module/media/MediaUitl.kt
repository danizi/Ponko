package com.ponko.cn.module.media

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.Toast
import com.google.gson.Gson
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.*
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.CacheUtil.getPolycConfig
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.OnEnterListener
import com.xm.lib.media.base.XmVideoView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*


object MediaUitl {
    private const val TAG = "MediaUitl"
    private var handler = object : Handler(Looper.getMainLooper()) {}

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
     * 三种选择 1 标准  2 高清 3超高清
     */
    private var QUALITY = 3

    /**
     * 视频地址选择 ps:调用接口有时候地址是空的
     */
    fun hls(section: List<VideoInfoCBean.DataBean>?): String {
        if (section?.isEmpty()!!) {
            throw NullPointerException("请检查保利威视接口返回的视频播放地址信息")
        }
        QUALITY = CacheUtil.getMediaType().toInt()
        if (section[0].hls.size < QUALITY) {
            QUALITY = section[0].hls.size
        }
        when (QUALITY) {
            1 -> BKLog.d(TAG, "流畅")
            2 -> BKLog.d(TAG, "高清")
            3 -> BKLog.d(TAG, "超高清")
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
        QUALITY = CacheUtil.getMediaType().toInt()
        if (section[0].filesize.size < QUALITY) {
            QUALITY = section[0].filesize.size
        }
        when (QUALITY) {
            1 -> BKLog.d(TAG, "流畅 ${section[0].filesize[0]}")
            2 -> BKLog.d(TAG, "高清 ${section[0].filesize[1]}")
            3 -> BKLog.d(TAG, "超高清 ${section[0].filesize[2]}")
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
        QUALITY = CacheUtil.getMediaType().toInt()
        return when (QUALITY) {
            1 -> section.filesize1.toInt()
            2 -> section.filesize2.toInt()
            3 -> section.filesize3.toInt()
            else -> {
                section.filesize3.toInt()
            }
        }
    }

    private fun getPtime(con: URLConnection): String {
        val serviceData = con.date
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("GMT+08")
        val ee = sdf.format(Date())

        val millionSeconds = sdf.parse(ee).time
        BKLog.e(TAG, "本地毫秒:$millionSeconds")
        BKLog.e(TAG, "外部毫秒:$serviceData")
        val date = Date(millionSeconds)
        BKLog.e(TAG, "本地日期:${sdf.format(date)}")
        BKLog.e(TAG, "服务器日期:${sdf.format(Date(serviceData))}")
        if (serviceData <= 0) {
            return millionSeconds.toString()
        }
        return serviceData.toString()
    }

    /**
     * 通过vid获取视频信息
     * @param vid        视频唯一标识         26de49f8c22abafd8adc1b49246262c6_2
     * @param userid     保利威视提供唯一标识 26de49f8c2
     * @param secretKey  保利威视提供的key    ETd98zg5Ka
     */
    @Deprecated("最好是使用下面的")
    fun getUrlByVid(vid: String?, userid: String, secretKey: String, listener: OnVideoInfoListener?) {
//        if (!NetworkUtil.isNetworkConnected(PonkoApp.activityManager.getTopActivity())) {
//            listener?.onFailure()
//            ToastUtil.show("获取播放数据失败,当前无网络...")
//        }
        Thread(Runnable {
            try {
                val timeUrl = URL("http://www.ntsc.ac.cn")
                val con = timeUrl.openConnection() as HttpURLConnection
                con.readTimeout = 10000
                con.connectTimeout = 10000
                con.connect()
//            if (con.responseCode == 200) {
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
                //val ptime: String = System.currentTimeMillis().toString()
                val ptime: String = getPtime(con)
                val sign = SHA1Util.hexString(SHA1Util.eccryptSHA1("format=$format&ptime=$ptime&vid=$vid$key")).toUpperCase()
                val client = OkHttpClient.Builder().build()
                val request = Request.Builder()
                        .url("$url?format=$format&ptime=$ptime&vid=$vid&sign=$sign")
                        .post(RequestBody.create(null, "")) //传递一个nullrequestBody
                        .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        listener?.onFailure()
                        ToastUtil.show("获取播放数据失败:${e.message}...")
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        //BKLog.d(TAG, "保利威视视频详细信息:" + response.body()!!.string())
                        val tempResponse = response.body()?.string()
                        val jsonObj = JSONObject(tempResponse)
                        if (200 == jsonObj["code"]) {
                            listener?.onSuccess(Gson().fromJson(tempResponse, VideoInfoCBean::class.java))
                        } else {
                            Looper.prepare()
                            DialogUtil.show(PonkoApp.activityManager.getTopActivity()!!, "提示", "播放失败，请检查手机系统时间是否与网络同步。", false, object : OnEnterListener {
                                override fun onEnter(dlg: AlertDialog) {
                                    dlg.dismiss()
                                }
                            }, null)
//                            DialogUtil.show(PonkoApp.activityManager.getTopActivity()!!, "请求参数", "" +
//                                    "获取播放信息失败，请联系技术人员处理。\n" +
//                                    "请求参数:$url?format=$format&ptime=$ptime&vid=$vid&sign=$sign\n" +
//                                    "错误信息:$tempResponse" +
//                                    "", false, object : OnEnterListener {
//                                override fun onEnter(dlg: AlertDialog) {
//                                    dlg.dismiss()
//                                }
//                            }, null)
                            Looper.loop()
                            listener?.onFailure()
                        }
                    }
                })
            } catch (e: Exception) {
                listener?.onFailure()
                //Looper.prepare()
                ToastUtil.show("获取视频信息失败:${e.message}")
                //Looper.loop()
                listener?.onFailure()
            }
        }).start()
    }

    /**
     * 通过vid获取视频信息
     */
    fun getUrlByVid(vid: String?, listener: MediaUitl.OnPlayUrlListener?) {
        //获取保利威视的视频配置信息 ps:在请求首页接口时就保存了
        val polyvBean = Gson().fromJson(getPolycConfig(), MainCBean.PolyvBean::class.java)
        getUrlByVid(vid, polyvBean.user_id, polyvBean.secret_key, object : MediaUitl.OnVideoInfoListener {
            override fun onFailure() {
                handler.post {
                    listener?.onFailure()
                }
                BKLog.e(TAG, "获取网络视频地址失败")
            }

            override fun onSuccess(videoInfo: VideoInfoCBean) {
                val m3u8 = hls(videoInfo.data)
                val total = fileSize(videoInfo.data)
                if (m3u8.startsWith("https")) {
                    ToastUtil.show("当前播放器不支持https播放地址,请联系相关技术人员。")
                } else {
                    BKLog.e("获取当前播放地址成功:$m3u8")
                }
                //Thread.sleep(15000)
                handler.post {
                    listener?.onSuccess(m3u8, total)
                }
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
    fun getM3u8Url(vid: String?, listener: MediaUitl.OnPlayUrlListener?) {
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
     * 初始化播放器View
     */
    fun initXmVideoView(xmVideoView: XmVideoView?, context: Context) {
        //绑定的页面
        val preUrl = ""
        val playUrl = ""
        val attachmentPre = AttachmentPre(context, preUrl)
        attachmentPre.url = playUrl
        val attachmentControl = AttachmentControl(context)
        val attachmentGesture = AttachmentGesture(context)
        val attachmentComplete = AttachmentComplete(context)
        xmVideoView?.bindAttachmentView(attachmentPre, "attachmentPre")      //预览附着页面
        xmVideoView?.bindAttachmentView(attachmentControl, "AttachmentControl")  //控制器附着页面
        xmVideoView?.bindAttachmentView(attachmentGesture, "attachmentGesture")  //手势附着页面(调节亮度和音量)
        xmVideoView?.bindAttachmentView(attachmentComplete, "attachmentComplete") //播放完成附着页面
        //播放器回调观察者
        xmVideoView?.addPlayerObserver(attachmentPre)
        xmVideoView?.addPlayerObserver(attachmentControl)
        xmVideoView?.addPlayerObserver(attachmentGesture)
        xmVideoView?.addPlayerObserver(attachmentComplete)
        //手势观察者
        xmVideoView?.addGestureObserver(attachmentPre)
        xmVideoView?.addGestureObserver(attachmentControl)
        xmVideoView?.addGestureObserver(attachmentGesture)
        xmVideoView?.addGestureObserver(attachmentComplete)
        //各种状态（断网、音量、电话、插上耳机、电量...）观察者
        xmVideoView?.addPhoneStateObserver(attachmentPre)
        xmVideoView?.addPhoneStateObserver(attachmentControl)
        xmVideoView?.addPhoneStateObserver(attachmentGesture)
        xmVideoView?.addPhoneStateObserver(attachmentComplete)
    }

    /**
     * 构建播放列表数据 - 学习详情页面
     */
    fun buildPlayListByStudy(coursesDetailCBean: CoursesDetailCBean?): MediaBean {
        //播放列表信息传递到控制器中去
        val isPay = coursesDetailCBean?.isPossess
        val mediaInfos = ArrayList<MediaBean.MediaInfo>()
        for (chapter in coursesDetailCBean?.chapters!!) {
            for (section in chapter.sections) {
                val isFree = section.isFree
                val vid = section.vid
                val name = section.name
                val duration = section.duration.toInt()
                val progress = section.progress_duration
                val avatar = section.avatar
                mediaInfos.add(MediaBean.MediaInfo(isPay, isFree, name, vid, duration, progress, avatar))
            }
        }
        return MediaBean(mediaInfos)
    }

    /**
     * 构建播放列表数据 - 学习免费页面
     */
    fun buildPlayListByFree(detailCBean: DetailCBean?): MediaBean {
        //val isPay = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![0].isIs_vip) || (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![1].isIs_vip)
        val isPay = false
        val mediaInfos = ArrayList<MediaBean.MediaInfo>()
        for (chapter in detailCBean?.chapters!!) {
            for (section in chapter.sections) {
                val isFree = section.isFree
                val vid = section.vid
                val name = section.sectionName
                val duration = section.duration.toInt()
                val progress = 0
                val avatar = section.avatar
                mediaInfos.add(MediaBean.MediaInfo(isPay, isFree, name, vid, duration, progress, avatar))
            }
        }
        return MediaBean(mediaInfos)
    }


    /**
     * 复制到剪切板上
     */
    private fun clip(clipStr: String, context: Context) {
        val myClipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("text", clipStr)
        myClipboard.primaryClip = myClip
        Toast.makeText(context, "已复制:$clipStr", Toast.LENGTH_SHORT).show()
    }

    /**
     * 测试
     */
    @JvmStatic
    fun main(args: Array<String>) {
        //getUrlByVid("26de49f8c22abafd8adc1b49246262c6_2", "26de49f8c2", "ETd98zg5Ka", null)
    }
}