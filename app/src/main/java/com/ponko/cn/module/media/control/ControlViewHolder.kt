package com.ponko.cn.module.media.control

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.view.View
import com.ponko.cn.bean.MediaBean
import com.xm.lib.common.helper.TimerHelper
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.attachment.control.ControlViewHolder
import com.xm.lib.media.base.XmMediaPlayer
import com.xm.lib.media.base.XmVideoView

/**
 * 播放器控制器 - View抽象基类ViewHolder
 */
abstract class ControlViewHolder : IDisplay, IControlViewHolder {
    protected val TAG = "ControlViewHolder"
    /**
     * 进度定时器
     */
    private var progressTimer: TimerHelper? = TimerHelper()
    /**
     * 控制页面隐藏定时器
     */
    private var controlViewHideTimer: TimerHelper? = TimerHelper()
    /**
     * 播放器控制页面
     */
    var attachmentControl: AttachmentControl? = null
    /**
     * 是否处于水平滑动
     */
    var isHorizontalSlide = false
    /**
     * 是否可以水平滑动
     */
    var isLock = false
    /**
     * 是否处理点击状态
     */
    var isClick = false
    /**
     * 保存用户拖动进度 单位:%
     */
    var progress = 0
    /**
     * 页面View
     */
    var rootView: View? = null
    /**
     * 分享View
     */
    var shareView: View? = null
    /**
     * 播放图片资源
     */
    var playResID = 0
    /**
     * 暂停图片资源
     */
    var pauseResID = 0
    /**
     * 保存竖屏播放器View信息
     */
    var portraitXmVideoViewRect: Rect? = null
    /**
     * 窗口实例
     */
    protected var activity: Activity? = null
    /**
     * 播放器
     */
    protected var mediaPlayer: XmMediaPlayer? = null
    /**
     * 播放器view
     */
    protected var xmVideoView: XmVideoView? = null
    protected var screenW = 0
    protected var screenH = 0
    /**
     * 屏幕状态监听
     */
    var listener: ControlViewHolder.OnScreenStateListener? = null
    /**
     * 控制器页面是否显示
     */
    var isControlViewShow = false

    override fun bind(attachmentControl: AttachmentControl?) {
        this.attachmentControl = attachmentControl
    }

    override fun showControlView() {
        rootView?.visibility = View.VISIBLE
        showTop()
        showBottom()
        showPlayList()
        isControlViewShow = true
    }

    override fun hideControlView() {
        hideTop()
        hideBottom()
        hideProgress()
        hidePlayList()
        hidePlayListAni()
        isControlViewShow = false
    }

    override fun progressTimerStart(period: Long) {
        progressTimer?.start(object : TimerHelper.OnPeriodListener {
            @SuppressLint("SetTextI18n")
            override fun onPeriod() {
                val mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
                if (mediaPlayer == null) {
                    BKLog.e(TAG, "progressTimerStart() mediaPlayer is null stop progressTimer!!!")
                    progressTimer?.stop()
                    return
                }

                val pos = mediaPlayer.getCurrentPosition()
                val duration = mediaPlayer.getDuration()
                if (pos > duration) {
                    progressTimer?.stop()
                    return
                }
                updateProgress(pos)
            }
        }, period)
    }

    override fun progressTimerStop() {
        progressTimer?.stop()
    }

    override fun startDelayTimerHideControlView(delay: Int) {
        controlViewHideTimer?.start(object : TimerHelper.OnDelayTimerListener {
            override fun onDelayTimerFinish() {
                if (isLock) {

                } else {
                    hideControlView()
                }
            }
        }, delay.toLong())
    }

    override fun stopDelayTimerHideControlView() {
        controlViewHideTimer?.stop()
    }


//    /**
//     * 点击屏幕控制界面 显示和隐藏状态来回切换
//     */
//    abstract fun showOrHideControlView()
//
//    /**
//     * 显示播放器控制界 PS : 只显示顶部和底部，按需显示”
//     */
//    open fun showControlView() {
//        rootView?.visibility = View.VISIBLE
//        showTop()
//        showBottom()
//        showPlayList()
//        isControlViewShow = true
//    }
//
//    /**
//     * 显示进度条
//     */
//    abstract fun showProgress()
//
//    /**
//     * 显示加载条
//     */
//    abstract fun showLoading()
//
//    /**
//     * 显示顶部View
//     */
//    abstract fun showTop()
//
//    /**
//     * 显示底部View
//     */
//    abstract fun showBottom()
//
//    /**
//     * 隐藏播放器控制界面，PS : 横竖切屏所要隐藏的界面块不同，需要特定操作请覆盖写该方法
//     */
//    open fun hideControlView() {
//        //rootView?.visibility = View.GONE
//        hideTop()
//        hideBottom()
//        //hideLoading()
//        hideProgress()
//        hidePlayList()
//        isControlViewShow = false
//    }
//
//    abstract fun hideProgress()
//    abstract fun hideLoading()
//    abstract fun hideTop()
//    abstract fun hideBottom()
//
//    /**
//     * 绑定播放器
//     */
//    open fun bind(attachmentControl: AttachmentControl?) {
//        this.attachmentControl = attachmentControl
//    }
//
//    /**
//     * 更新播放器进度UI界面 PS:更新进度条、控制页面“居中进度文本”、底部进度条“居右进度文本”
//     * @param isSetProgress 如果手指未触碰seekBar控件则使用默认值，否则设置false
//     */
//    abstract fun updateProgress(pos: Long, isSetProgress: Boolean = true)
//
//    /**
//     * tingzhi
//     */
//    abstract fun horizontalSlideStopSeekTo()
//
//    /**
//     * 设置播放动作图片
//     */
//    abstract fun setActionResID(id: Int)
//
//    /**
//     * 设置缓冲进度 单位:%
//     */
//    abstract fun secondaryProgress(present: Int)
//
//    /**
//     * 设置播放进度 单位:%
//     */
//    abstract fun progress(present: Int)
//
//    /**
//     * 开启进度更新定时器
//     */
//    open fun progressTimerStart(period: Long) {
//        /*定时更新进度*/
//        progressTimer?.start(object : TimerHelper.OnPeriodListener {
//            @SuppressLint("SetTextI18n")
//            override fun onPeriod() {
//                val mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
//                if (mediaPlayer == null) {
//                    BKLog.e(TAG, "progressTimerStart() mediaPlayer is null ")
//                    progressTimer?.stop()
//                    return
//                }
//
//                val pos = mediaPlayer.getCurrentPosition()
//                val duration = mediaPlayer.getDuration()
//                if (pos > duration) {
//                    progressTimer?.stop()
//                    return
//                }
//                updateProgress(pos)
//            }
//        }, period)
//    }
//
//    /**
//     * 关闭定时更新进度进度定时器
//     */
//    fun progressTimerStop() {
//        progressTimer?.stop()
//    }
//
//    /**
//     * 启动延时隐藏播放器界面定时器
//     */
//    fun startDelayTimerHideControlView(delay: Int) {
//        controlViewHideTimer?.start(object : TimerHelper.OnDelayTimerListener {
//            override fun onDelayTimerFinish() {
//                hideControlView()
//            }
//        }, delay.toLong())
//
//    }
//
//    /**
//     * 停止延时隐藏控制界面
//     */
//    fun stopDelayTimerHideControlView() {
//        controlViewHideTimer?.stop()
//    }
//
//    /**
//     * 显示播放列表 - 横向
//     */
//    open fun showPlayList() {}
//
//    /**
//     * 显示播放列表 - 动画
//     */
//    open fun showPlayListAni() {}
//
//    /**
//     * 隐藏播放列表 - 横向
//     */
//    open fun hidePlayList() {}
//
//    /**
//     * 隐藏播放列表 - 动画
//     */
//    open fun hidePlayListAni() {}
//
//    /**
//     * 横竖屏状态监听
//     */
//    @Deprecated("")
//    interface OnScreenStateListener {
//        fun onState(type: String)
//    }
//
//    @Deprecated("可以完全去掉了")
//    abstract fun setShareListener(listener: View.OnClickListener)
//
//    /**
//     * 设置分享信息
//     */
//    abstract fun setShareInfo(share: MediaBean.ShareBean)
//
//    /**
//     * 播放列表
//     */
//    abstract fun setMediaInfo(info: MediaBean)
//
//    /**
//     * 设置标题
//     */
//    abstract fun setTitle(title: String)
//
//    /**
//     * 是否显示控制页面
//     */
//    abstract fun isShowControlView(): Boolean
}

/**
 * 控制器View一般方法
 */
interface IControlViewHolder {
    /**
     * 绑定播放控制器
     */
    fun bind(attachmentControl: AttachmentControl?)

    /**
     * 设置标题
     */
    fun setTitle(title: String)

    /**
     * 是否显示控制页面
     */
    fun isShowControlView(): Boolean

    /**
     * 设置分享信息
     */
    fun setShareInfo(share: MediaBean.ShareBean)

    /**
     * 播放列表 PS：一般用于横屏
     */
    fun setMediaInfo(info: MediaBean)

    fun setPlayList(listener: OnPlayListItemClickListener?)

    fun selectPlayList(index: Int)

    @Deprecated("可以完全去掉了")
    abstract fun setShareListener(listener: View.OnClickListener)
}

/**
 * 控制器View展示相关
 */
interface IDisplay {
    /**
     * 更新播放器进度UI界面 PS:更新进度条、控制页面“居中进度文本”、底部进度条“居右进度文本”
     * @param isSetProgress 如果手指未触碰seekBar控件则使用默认值，否则设置false
     */
    fun updateProgress(pos: Long, isSetProgress: Boolean = true)

    /**
     * 横向滑动停止
     */
    fun horizontalSlideStopSeekTo()

    /**
     * 设置播放动作资源
     * @param id 资源id
     */
    fun setActionResID(id: Int)

    /**
     * 设置播放进度 单位:%
     */
    fun progress(present: Int)

    /**
     * 设置缓冲进度 单位:%
     */
    fun secondaryProgress(present: Int)

    /**
     * 开启进度更新定时器
     * @param period 轮询时间一秒 单位：毫秒
     */
    fun progressTimerStart(period: Long)

    /**
     * 关闭定时更新进度进度定时器
     */
    fun progressTimerStop()

    /**
     * 启动延时隐藏播放器界面定时器
     */
    fun startDelayTimerHideControlView(delay: Int)

    /**
     * 停止延时隐藏控制界面
     */
    fun stopDelayTimerHideControlView()

    /**
     * 点击屏幕控制界面 显示和隐藏状态来回切换
     */
    fun showOrHideControlView()

    /**
     * 显示播放列表 - 动画 PS:一般用于横屏
     */
    fun showPlayListAni()

    /**
     * 隐藏播放列表 - 动画
     */
    fun hidePlayListAni()

    /**
     * 显示播放列表 - 横向 PS:一般用于横屏
     */
    fun showPlayList()

    /**
     * 隐藏播放列表 - 横向 PS:一般用于横屏
     */
    fun hidePlayList()

    fun showControlView()//显示播放器控制界 PS : 只显示顶部和底部，按需显示”
    fun showProgress()
    fun showLoading()
    fun showTop()
    fun showBottom()

    fun hideControlView()//隐藏播放器控制界面，PS : 横竖切屏所要隐藏的界面块不同，需要特定操作请覆盖写该方法
    fun hideProgress()
    fun hideLoading()
    fun hideTop()
    fun hideBottom()

}

/**
 * 横竖屏状态监听
 */
interface OnScreenStateListener {
    fun onState(type: String)
}