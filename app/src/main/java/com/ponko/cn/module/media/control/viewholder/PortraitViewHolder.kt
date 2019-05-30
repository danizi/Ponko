package com.ponko.cn.module.media.control.viewholder

import android.annotation.SuppressLint
import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.*
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.module.media.control.ControlViewHolder
import com.ponko.cn.utils.ShareUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.OnPlayListItemClickListener

/**
 * 竖屏控制器View界面
 */
class PortraitViewHolder private constructor(private val view: View, private val clPortraitTop: ConstraintLayout?, private val ivBack: ImageView?, private val tvTitle: TextView?, private val ivListener: ImageView?, private val ivMiracast: ImageView?, private val ivShare: ImageView?, private val ivMore: ImageView?, private val clPortraitBottom: ConstraintLayout?, private val ivAction: ImageView?, private val seekBar: SeekBar?, private val tvTime: TextView?, private val ivScreenFull: ImageView?, private val clSeek: ConstraintLayout?, private val tvTime2: TextView?, private val pbLoading: ProgressBar?) : ControlViewHolder() {

    private var share: MediaBean.ShareBean? = null

    companion object {
        fun create(rootView: View?): PortraitViewHolder {
            val clPortraitTop = rootView?.findViewById<View>(R.id.cl_portrait_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivListener = rootView.findViewById<View>(R.id.iv_listener) as ImageView
            val ivMiracast = rootView.findViewById<View>(R.id.iv_miracast) as ImageView
            val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clPortraitBottom = rootView.findViewById<View>(R.id.cl_portrait_bottom) as ConstraintLayout
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val ivScreenFull = rootView.findViewById<View>(R.id.iv_screen_full) as ImageView
            val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
            val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            return PortraitViewHolder(rootView, clPortraitTop, ivBack, tvTitle, ivListener, ivMiracast, ivShare, ivMore, clPortraitBottom, ivAction, seekBar, tvTime, ivScreenFull, clSeek, tvTime2, pbLoading)
        }
    }

    init {
        this.rootView = view
    }

    override fun showPlayListAni() {

    }

    override fun hidePlayListAni() {

    }

    override fun showPlayList() {

    }

    override fun hidePlayList() {

    }

    override fun setTitle(title: String) {
        tvTitle?.text = title
    }

    override fun setMediaInfo(info: MediaBean) {
    }

    override fun setShareInfo(share: MediaBean.ShareBean) {
        this.share = share
    }

    override fun horizontalSlideStopSeekTo() {
        val seekPos = mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
        xmVideoView = attachmentControl?.xmVideoView
        initDisplay()
        size()
        initEvent()
    }

    private fun size() {
        activity = attachmentControl?.context as Activity
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
        if (screenH > screenW) {
            val temp = screenW
            screenW = screenH
            screenH = temp
        }
    }

    private fun initDisplay() {
        val mediaPlayer = (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).xmVideoView?.mediaPlayer
        if (mediaPlayer?.isPlaying() == true) {
            ivAction?.setImageResource(pauseResID)
        } else {
            ivAction?.setImageResource(playResID)
        }
    }

    private fun initEvent() {
        //顶部
        ivBack?.setOnClickListener {
            clickBack()
        }
        ivListener?.setOnClickListener {
            clickListener()
        }
        ivMiracast?.setOnClickListener {
            clickMiracast()
        }
        ivShare?.setOnClickListener {
            clickShare()
        }
        //底部
        ivAction?.setOnClickListener {
            clickAction()
        }
        ivScreenFull?.setOnClickListener {
            clickScreenFull()
        }
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * 设置progress 属性也会触发，所有设置进度完成的时候应该在DOWN_UP事件回调中设置播放器播放进度
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressChanged(seekBar, progress, fromUser)
            }

            /**
             * 只有手指触控了滑块触发
             */
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                startTrackingTouch(seekBar)
            }

            /**
             * 只有手指触控了滑块释放后触发
             */
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                stopTrackingTouch(seekBar)
            }
        })
    }

    private fun clickBack() {
        activity?.finish()
        BKLog.d(TAG, "点击退出")
    }

    private fun clickListener() {
        Toast.makeText(activity, "耳机模式", Toast.LENGTH_SHORT).show()
        BKLog.d(TAG, "点击耳机模式")
    }

    private fun clickMiracast() {
        Toast.makeText(activity, "投屏", Toast.LENGTH_SHORT).show()
        BKLog.d(TAG, "点击投屏")
    }

    private fun clickShare() {
        ShareUtil.showShareDlg(activity, this.share?.url, this.share?.title, this.share?.description)
        BKLog.d(TAG, "点击分享")
    }

    private fun clickAction() {
        try {
            if (mediaPlayer?.isPlaying() == true) {
                ivAction?.setImageResource(playResID)
                mediaPlayer?.pause()
                BKLog.d(TAG, "点击暂停")
            } else {
                ivAction?.setImageResource(pauseResID)
                mediaPlayer?.start()
                BKLog.d(TAG, "点击播放")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clickScreenFull() {
        //横屏 高度 < 宽度
        ScreenUtil.setLandscape(activity)  //设置横屏
        ScreenUtil.hideStatusBar(activity) //隐藏系统状态栏

        //xmVideoView?.layout(0, 0, screenW, screenH)    //设置宽高
        xmVideoView?.layoutParams?.height = screenH
        xmVideoView?.layoutParams?.width = screenW + ScreenUtil.getStatusBarHeight(activity)

        hideControlView()                  //隐藏控制界面  PS : 或者删除
        listener?.onState(AttachmentControl.LANDSCAPE)
        BKLog.d(TAG, "点击全屏")
    }

    private fun progressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        this@PortraitViewHolder.progress = progress
        if (isHorizontalSlide) {
            //用户在屏幕水平滑动，但未触碰到seekbar时
            BKLog.i(TAG, "“未”触碰到Seekbar，滑动中... progress:$progress")
        } else {
            //用户触碰了seekbar或者定时器一直设置progress属性值时
            val pos = ((progress.toFloat() / 100f) * mediaPlayer?.getDuration()!!).toLong()
            updateProgress(pos, false)
            BKLog.i(TAG, "触碰到Seekbar，滑动中... progress:$progress")
        }
    }

    private fun startTrackingTouch(seekBar: SeekBar?) {
        showControlView()
        showProgress()
        progressTimerStop()
        progress = 0
        BKLog.d(TAG, "开始触发滑动 progress:$progress")
    }

    private fun stopTrackingTouch(seekBar: SeekBar?) {
        hideControlView()
        horizontalSlideStopSeekTo()
        progressTimerStart(period = 1000)
        BKLog.d(TAG, "结束触发滑动 progress:$progress")
    }

    override fun setShareListener(listener: View.OnClickListener) {
        ivShare?.setOnClickListener(listener)
    }

    override fun isShowControlView(): Boolean {
        return clPortraitBottom?.visibility == View.VISIBLE
    }

    override fun showOrHideControlView() {
        if (clPortraitBottom?.visibility == View.VISIBLE) {
            hideControlView()
            BKLog.d(TAG, "控制器View隐藏")
        } else {
            showControlView()
            BKLog.d(TAG, "控制器View显示")
        }
    }

    override fun showLoading() {
        pbLoading?.visibility = View.VISIBLE
        BKLog.d(TAG, "显示加载")
    }

    override fun showTop() {
        clPortraitTop?.visibility = View.VISIBLE
        BKLog.d(TAG, "显示顶部")
    }

    override fun showBottom() {
        clPortraitBottom?.visibility = View.VISIBLE
        BKLog.d(TAG, "显示底部")
    }

    override fun showProgress() {
        clSeek?.visibility = View.VISIBLE
        BKLog.d(TAG, "显示进度")
    }

    override fun hideLoading() {
        pbLoading?.visibility = View.GONE
        BKLog.d(TAG, "隐藏进度")
    }

    override fun hideTop() {
        clPortraitTop?.visibility = View.GONE
        BKLog.d(TAG, "隐藏顶部")
    }

    override fun hideBottom() {
        clPortraitBottom?.visibility = View.GONE
        BKLog.d(TAG, "隐藏底部")
    }

    override fun hideProgress() {
        clSeek?.visibility = View.GONE
        BKLog.d(TAG, "隐藏进度条")
    }

    override fun setActionResID(id: Int) {
        try {
            ivAction?.setImageResource(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hideControlView() {
        BKLog.d(TAG, "隐藏控制器View")
        hideBottom()
        //hideLoading()
        hideProgress()
    }

    override fun secondaryProgress(present: Int) {
        seekBar?.secondaryProgress = present
        BKLog.d(TAG, "显示缓存条present:$present")
    }

    override fun progress(present: Int) {
        seekBar?.progress = present
        BKLog.d(TAG, "进度条present:$present")
    }

    @SuppressLint("SetTextI18n")
    override fun updateProgress(pos: Long, isSetProgress: Boolean) {
        if (mediaPlayer == null) {
            BKLog.e(TAG, "updateProgress() mediaPlayer is null")
            return
        }
        val duration = mediaPlayer?.getDuration()!!
        if (isSetProgress) {
            seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
        }
        if (pos in 0..duration) {
            tvTime?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
            tvTime2?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
        }
    }

    override fun setPlayList(listener: OnPlayListItemClickListener?) {
    }

    override fun selectPlayList(index: Int) {
    }
}
