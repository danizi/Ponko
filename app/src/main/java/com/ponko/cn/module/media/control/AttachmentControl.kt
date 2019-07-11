package com.ponko.cn.module.media.control

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.viewholder.PortraitViewHolder
import com.ponko.cn.module.media.control.viewholder.landscape.LandscapeViewHolder
import com.ponko.cn.utils.CacheUtil
import com.ponko.cn.utils.DialogUtil
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.common.http.NetworkUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.OnCancelListener
import com.xm.lib.component.OnEnterListener
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
class AttachmentControl(context: Context?) : BaseAttachmentView(context), IAttachmentControl {
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
    @Deprecated("")
    private var share: MediaBean.ShareBean? = null
    /**
     * 播放器配置信息
     */
    @Deprecated("")
    private var config: MediaBean.ConfigBean? = null

    @Deprecated("视频信息列表,不适用")
    var courseDetail: CoursesDetailCBean? = null

    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    init {
        //播放器回调监听
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

            override fun onError(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onError(mp, what, extra)
                DialogUtil.show(context!!, "提示", "播放错误码what:$what extra:$extra", true, null, null)
            }
        }
        //手势监听回调
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
                if (ui?.isControlViewShow!! || true) {
                    if (present > 0) {
                        ui?.hidePlayListAni()
                    } else {
                        ui?.showControlView()
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
        //手机状态回调
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        //保存此时播放器View的位置信息
        portraitXmVideoViewRect?.left = xmVideoView.left
        portraitXmVideoViewRect?.top = xmVideoView.top
        portraitXmVideoViewRect?.right = xmVideoView.right
        portraitXmVideoViewRect?.bottom = xmVideoView.bottom
        //添加竖屏控制器View
        addPortraitView()
    }

    override fun layoutId(): Int {
        return R.layout.attachment_control
    }

    override fun start(vid: String, progress: Int?, index: Int) {
        stop()
        MediaUitl.getM3u8Url(vid, object : MediaUitl.OnPlayUrlListener {
            override fun onFailure() {
                BKLog.e("获取视频失败")
                if (!NetworkUtil.isNetworkConnected(PonkoApp.app)) {
                    DialogUtil.show(context!!, "提示", "播放失败，当前没有网络...", true, null, null)
                }
            }

            override fun onSuccess(url: String, size: Int?) {
                if (NetworkUtil.is3GNet(context)) {
                    DialogUtil.show(context, "提示", "当前使用是手机流量,是否继续播放？", true, object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            play(url)
                            dlg.dismiss()
                        }
                    }, object : OnCancelListener {
                        override fun onCancel(dlg: AlertDialog) {
                            dlg.dismiss()
                        }
                    })
                } else {
                    play(url)
                }
            }

            private fun play(url: String) {
                xmVideoView?.start(url, true, progress)
                this@AttachmentControl.index = index
            }
        })
    }

    override fun stop() {
        xmVideoView?.mediaPlayer?.stop()
    }

    override fun pause() {
        xmVideoView?.pause()
    }

    override fun replay() {
        if (info?.mediaInfos?.size!! > index) {
            val mediaBean = this.info?.mediaInfos!![index]
            start(mediaBean.vid!!, mediaBean.progress_duration, index)
        } else {
            ToastUtil.show("重播失败...")
            BKLog.d("重播失败")
        }
    }

    override fun next() {
        index++
        if (info?.mediaInfos?.size!! > index) {
            val mediaBean = this.info?.mediaInfos!![index]
            listener?.item(mediaBean.vid!!, mediaBean.progress_duration, null, index)
            //start(mediaBean.vid!!, mediaBean.progress_duration, index)
        } else {
            ToastUtil.show("已经是最后一集了...")
            BKLog.d("播放下一集失败")
        }
    }

    override fun pre() {
        BKLog.d("播放上一集失败")
    }

    override fun setTitle(title: String) {
        this.title = title
        ui?.setTitle(title)
    }

    override fun addLandscapeView(visibility: Int?) {
        //获取横屏View
        val landscapeView = getView(R.layout.attachment_control_landscape)
        //是否显示
        landscapeView.visibility = View.GONE
        //添加到控制器容器中
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        //创建控制器View ViewHolder
        ui = LandscapeViewHolder.create(landscapeView)
        //设置播放暂停资源
        ui?.playResID = R.mipmap.media_control_play
        ui?.pauseResID = R.mipmap.media_control_pause
        //将控制器传入LandscapeViewHolder中
        ui?.bind(this)
        //竖屏播放器View信息传入LandscapeViewHolder中
        ui?.portraitXmVideoViewRect = portraitXmVideoViewRect
        //横竖切屏监听
        ui?.listener = object : com.xm.lib.media.attachment.control.ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                //删除所有子View ps:子View包含横屏or竖屏View
                this@AttachmentControl.removeAllViews()
                //选择添加指定控制器View
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
        //传递进度更新计时器轮询间隔时间
        ui?.progressTimerStart(period.toLong())
        //设置播放列表信息
        ui?.setMediaInfo(this.info!!)
        //设置播放列表item点击监听
        //(ui as LandscapeViewHolder).setPlayList(listener)
        ui?.setPlayList(listener)
        //选中列表中的那个一个item
//        (ui as LandscapeViewHolder).selectPlayList(this.index)
        ui?.selectPlayList(this.index)
        //设置标题
        ui?.setTitle(title)
    }

    override fun addPortraitView(visibility: Int?) {
        //获取竖屏View
        val portraitView = getView(R.layout.attachment_control_portrait)
        //是否显示
        portraitView.visibility = View.GONE
        //添加到控制器容器中
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        //创建控制器View ViewHolder
        ui = PortraitViewHolder.create(portraitView)
        //设置播放暂停资源
        ui?.playResID = R.mipmap.media_control_play
        ui?.pauseResID = R.mipmap.media_control_pause
        //将控制器传入PortraitViewHolder中
        ui?.bind(this)
        //横竖切屏监听
        ui?.listener = object : com.xm.lib.media.attachment.control.ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                //删除所有子View ps:子View包含横屏or竖屏View
                this@AttachmentControl.removeAllViews()
                //选择添加指定控制器View
                when (type) {
                    PORTRAIT -> {
                        addPortraitView(View.VISIBLE)
                    }
                    LANDSCAPE -> {
                        addLandscapeView(View.VISIBLE)
                        xmVideoView?.setSufaceViewSize()
                    }
                }
            }
        }
        //传递进度更新计时器轮询间隔时间
        ui?.progressTimerStart(period.toLong())
        //设置标题
        ui?.setTitle(title)
    }

    override fun showLoading() {
        ui?.showLoading()
    }

    override fun isShowControlView(): Boolean {
        return ui?.isShowControlView()!!
    }

    override fun updateListItem(index: Int) {
        if (ui is LandscapeViewHolder) {
            (ui as LandscapeViewHolder).selectPlayList(index)
        }
    }

    override fun setOnPlayListItemClickListener(listener: OnPlayListItemClickListener) {
        this.listener = listener
    }

    override fun setMediaInfo(info: MediaBean) {
        this.info = info
    }

    override fun setConfigInfo(config: MediaBean.ConfigBean) {
        this.config = config
    }

    override fun setShareInfo(share: MediaBean.ShareBean) {
        this.share = share
        ui?.setShareInfo(share)
    }

    override fun selectPlayList(index: Int) {
        ui?.selectPlayList(index)
    }

    override fun setPlayList(listener: OnPlayListItemClickListener?) {
        ui?.setPlayList(listener)
    }

    override fun setShareListener(listener: OnClickListener) {
        ui?.setShareListener(listener)
    }

    override fun ratio(ratioValue: Int) {
        CacheUtil.putMediaType(ratioValue.toString())
        val progress = xmVideoView?.mediaPlayer?.getCurrentPosition()?.toInt()
        stop()
        if (info?.mediaInfos?.size!! > index) {
            val mediaBean = this.info?.mediaInfos!![index]
            start(mediaBean.vid!!, progress, index)
        } else {
            ToastUtil.show("重播失败...")
            BKLog.d("重播失败")
        }
    }
}

/**
 * 提供接口
 */
interface IAttachmentControl {

    /**
     * 播放，请求视频信息，获取播放地址再播放
     * @param vid 视频唯一标识
     * @param progress 指定进度播放
     * @param index 播放列表下标
     */
    fun start(vid: String, progress: Int? = 0, index: Int)

    /**
     * 停止播放
     */
    fun stop()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 重播播放
     */
    fun replay()

    /**
     * 播放下一集
     */
    fun next()

    /**
     * 播放上一集
     */
    fun pre()

    /**
     * 设置标题
     * @param title 标题
     */
    fun setTitle(title: String)

    /**
     * 添加横屏控制View
     * @param visibility 添加前是否显示
     */
    fun addLandscapeView(visibility: Int? = View.GONE)

    /**
     * 添加竖屏控制View
     * @param visibility 添加前是否显示
     */
    fun addPortraitView(visibility: Int? = View.GONE)

    /**
     * 显示加载
     */
    fun showLoading()

    /**
     * 播放器View控制是否显示
     */
    fun isShowControlView(): Boolean

    /**
     * 刷新播放列表
     * @param index 更新列表位置
     */
    fun updateListItem(index: Int)

    /**
     * 点击播放列表监听
     * @param listener 监听
     */
    fun setOnPlayListItemClickListener(listener: OnPlayListItemClickListener)

    /**
     * 设置播放信息
     * @param info 播放列表信息
     */
    fun setMediaInfo(info: MediaBean)

    /**
     * 设置配置信息
     * @param config 配置信息 例如是否后台播放、播放速度、画布比例 ...
     */
    fun setConfigInfo(config: MediaBean.ConfigBean)

    /**
     * 设置分享信息
     * @param share 分享信息
     */
    fun setShareInfo(share: MediaBean.ShareBean)

    /**
     * 设置列表点击监听 PS:一般用于横屏
     */
    fun selectPlayList(index: Int)

    /**
     * 选中播放列表的第几个item PS:一般用于横屏
     */
    fun setPlayList(listener: OnPlayListItemClickListener?)

    /**
     * 设置播放分辨率
     * @param ratioValue 移除从高到低3 2 1
     */
    fun ratio(ratioValue: Int)

    @Deprecated("完全可以去掉")
    fun setShareListener(listener: View.OnClickListener)
}