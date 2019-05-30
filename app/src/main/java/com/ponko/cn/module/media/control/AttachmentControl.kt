package com.ponko.cn.module.media.control

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.viewholder.LandscapeViewHolder
import com.ponko.cn.module.media.control.viewholder.PortraitViewHolder
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.BaseAttachmentView
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


/**
 * 控制器附着View
 */
class AttachmentControl(context: Context?) : BaseAttachmentView(context) {
    /**
     * 课程标题
     */
    private var title: String = ""
    /**
     * 点击列表监听
     */
    private var listener: OnPlayListItemClickListener? = null
    /**
     * 播放视频列表中的哪一个视频下标
     */
    var index: Int = 0
    /**
     * 控制器View
     */
    var ui: ControlViewHolder? = null
    /**
     * 定时器周期时间，进度更新定时更新时间
     */
    private val period: Int = 1000
    /**
     * 点击屏幕后延时delay时间隐藏控制器页面
     */
    private val delay: Int = 5000
    /**
     * 视频信息列表
     */
    @Deprecated("不适用")
    var courseDetail: CoursesDetailCBean? = null
    /**
     * 保存竖屏视频大小信息
     */
    private var portraitXmVideoViewRect: Rect? = Rect()
    /**
     * 视频信息
     */
    private var info: MediaBean? = null
    /**
     * 分享信息
     */
    private var share: MediaBean.ShareBean? = null
    /**
     * 播放器配置信息
     */
    private var config: MediaBean.ConfigBean? = null

    //private var portraitXmVideoView: XmVideoView? = null
    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    init {
        observer = object : PlayerObserver {
            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.bringChildToFront(this@AttachmentControl)
            }


            override fun onSeekComplete(mp: IXmMediaPlayer) {
                super.onSeekComplete(mp)
                //ui?.hideLoading()
            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == IXmMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    ui?.setActionResID(R.mipmap.media_control_pause)
                    //ui?.progressTimerStart(period.toLong())
                }
                when (what) {
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        ui?.showLoading()
                    }
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        ui?.hideLoading()
                    }
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                ui?.secondaryProgress(percent)
            }


        }
        gestureObserver = object : GestureObserver {
            private var horizontalSlidePos: Long = -1  //记录手指滑动时，第一次播放进度。主要在回调中使用

            override fun onClick() {
                super.onClick()
                ui?.isClick = true
                ui?.showOrHideControlView()
            }

            override fun onDownUp() {
                super.onDownUp()
                horizontalSlidePos = -1
                //手指滑动设置进度释放处理进度
                if (ui?.isHorizontalSlide!!) {
                    ui?.isHorizontalSlide = false
                    ui?.hideControlView()
                    ui?.horizontalSlideStopSeekTo()
                    ui?.progressTimerStart(period.toLong())
                    ui?.horizontalSlideStopSeekTo()
                }

                if (ui?.isClick!!) {
                    ui?.isClick = false
                    ui?.startDelayTimerHideControlView(delay)
                }
            }

            override fun onScaleEnd(scaleFactor: Float) {
                super.onScaleEnd(scaleFactor)
                if (context == null) {
                    BKLog.e(TAG, "context is null")
                    return
                }

                if (ScreenUtil.isLandscape(context)) {

                }
            }

            override fun onVertical(type: String, present: Int) {
                super.onVertical(type, present)
                if (ui?.isControlViewShow!!) {
                    if (present > 0) {
                        ui?.hidePlayListAni()
                    } else {
                        ui?.showPlayListAni()
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onHorizontal(present: Int) {
                super.onHorizontal(present)
                //记录手指滑动时播放器的位置 ps:只记录一次
                if (horizontalSlidePos == -1L) {
                    horizontalSlidePos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                }
                //手指处于水平滑动中
                ui?.isHorizontalSlide = true

                //停止所有的计时器
                ui?.stopDelayTimerHideControlView()
                ui?.progressTimerStop()

                //显示控制器界面
                ui?.updateProgress(horizontalSlidePos.toInt() + present * 1000L) // ps: 更新进度条 播放进度文本
                ui?.showProgress()
                ui?.showControlView()
            }
        }
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun layoutId(): Int {
        return R.layout.attachment_control
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        portraitXmVideoViewRect?.left = xmVideoView.left
        portraitXmVideoViewRect?.top = xmVideoView.top
        portraitXmVideoViewRect?.right = xmVideoView.right
        portraitXmVideoViewRect?.bottom = xmVideoView.bottom
        addPortraitView()
    }

    private fun addPortraitView(visibility: Int = View.GONE) {
        val portraitView = getView(R.layout.attachment_control_portrait)
        portraitView.visibility = visibility
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        ui = PortraitViewHolder.create(portraitView)
        ui?.playResID = R.mipmap.media_control_play
        ui?.pauseResID = R.mipmap.media_control_pause
        ui?.bind(this)
        ui?.listener = object : com.xm.lib.media.attachment.control.ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                this@AttachmentControl.removeAllViews()//删除所有子View ps:子View包含横屏or竖屏View
                when (type) {
                    PORTRAIT -> {
                        addPortraitView(View.VISIBLE)
                    }
                    LANDSCAPE -> {
                        addLandscapeView(View.VISIBLE)
                    }
                }
            }
        }
        ui?.progressTimerStart(period.toLong())
        ui?.setTitle(title)
    }

    private fun addLandscapeView(visibility: Int = View.GONE) {
        val landscapeView = getView(R.layout.attachment_control_landscape)
        landscapeView.visibility = visibility
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        ui = LandscapeViewHolder.create(landscapeView)
        ui?.playResID = R.mipmap.media_control_play
        ui?.pauseResID = R.mipmap.media_control_pause
        ui?.bind(this)
        ui?.portraitXmVideoViewRect = portraitXmVideoViewRect
        ui?.listener = object : com.xm.lib.media.attachment.control.ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                this@AttachmentControl.removeAllViews()//删除所有子View ps:子View包含横屏or竖屏View
                when (type) {
                    PORTRAIT -> {
                        addPortraitView(View.VISIBLE)
                    }
                    LANDSCAPE -> {
                        addLandscapeView(View.VISIBLE)
                    }
                }

            }
        }
        ui?.progressTimerStart(period.toLong())
        //设置播放列表信息
        ui?.setMediaInfo(this.info!!)
        //设置播放列表监听
        (ui as LandscapeViewHolder).setPlayList(listener)
        (ui as LandscapeViewHolder).selectPlayList(this.index)
        ui?.setTitle(title)
    }

    /**
     * 显示
     */
    fun showLoading() {
        ui?.showLoading()
    }

    fun pause() {
        xmVideoView?.pause()
    }

    /**
     * 播放，请求视频信息，获取播放地址再播放
     * @param vid 视频唯一标识
     * @param progress 指定进度播放
     * @param index 播放列表下标
     */
    fun start(vid: String, progress: Int? = 0, index: Int) {

        MediaUitl.getM3u8Url(vid, object : MediaUitl.OnPlayUrlListener {
            override fun onFailure() {
                BKLog.e("获取视频失败")
                ToastUtil.show("当前没有网络...")
            }

            override fun onSuccess(url: String, size: Int?) {
                xmVideoView?.start(url, true, progress)
                this@AttachmentControl.index = index
            }
        })
    }

    /**
     * 下一首
     */
    fun next() {
        index++
        if (info?.mediaInfos?.size!! > index) {
            val mediaBean = this.info?.mediaInfos!![index]
            listener?.item(mediaBean.vid!!, mediaBean.progress_duration, null, index)
            //start(mediaBean.vid!!, mediaBean.progress_duration, index)
        } else {
            ToastUtil.show("已经是最后一集了...")
            BKLog.d("播放下一首失败")
        }
    }

    /**
     * 重播失败
     */
    fun replay() {
        if (info?.mediaInfos?.size!! > index) {
            val mediaBean = this.info?.mediaInfos!![index]
            start(mediaBean.vid!!, mediaBean.progress_duration, index)
        } else {
            ToastUtil.show("重播失败...")
            BKLog.d("重播失败")
        }
    }

    /**
     * 设置播放信息
     */
    fun setMediaInfo(info: MediaBean) {
        this.info = info
    }

    /**
     * 设置分享信息
     */
    fun setShareInfo(share: MediaBean.ShareBean) {
        this.share = share
        ui?.setShareInfo(share)
    }

    @Deprecated("完全可以去掉")
    fun setShareListener(listener: View.OnClickListener) {
        ui?.setShareListener(listener)
    }

    /**
     * 设置配置信息
     */
    fun setConfigInfo(config: MediaBean.ConfigBean) {
        this.config = config
    }

    /**
     * 点击列表监听
     */
    fun setOnPlayListItemClickListener(listener: OnPlayListItemClickListener) {
        this.listener = listener
    }

    /**
     * 刷新
     */
    fun updateListItem(index: Int) {
        (ui as LandscapeViewHolder).selectPlayList(index)
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String) {
        this.title = title
        ui?.setTitle(title)
    }

    /**
     * 进度
     */
    fun isShowControlView(): Boolean {
        return ui?.isShowControlView()!!
    }
}