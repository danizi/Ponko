package com.ponko.cn.module.media.control.viewholder.landscape

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ponko.cn.bean.CoursesDetailCBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.module.media.control.ControlViewHolder
import com.ponko.cn.utils.ShareUtil
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.view.XmPopWindow

/**
 * 横屏界面
 */
class LandscapeViewHolder private constructor(private val view: View, private val clLandscapeTop: ConstraintLayout?, private val ivBack: ImageView?, private val tvTitle: TextView?, private val ivShare: ImageView?, private val ivMore: ImageView?, private val clLandscapeBottom: ConstraintLayout?, private val seekBar: SeekBar?, private val ivAction: ImageView?, private val tvTime: TextView?, private val tvRatio: TextView?, private val clSeek: ConstraintLayout?, private val tvTime2: TextView?, private val pbLoading: ProgressBar?, private val rv: RecyclerView?) : ControlViewHolder() {


    companion object {
        fun create(rootView: View?): LandscapeViewHolder {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
            val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView
            return LandscapeViewHolder(rootView, clLandscapeTop, ivBack, tvTitle, ivShare, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio, clSeek, tvTime2, pbLoading, rv)
        }
    }

    init {
        this.rootView = view
    }

    /**
     * 播放列表信息
     */
    private var mediaInfo: MediaBean? = null
    /**
     * 播放分享信息
     */
    private var share: MediaBean.ShareBean? = null
    /**
     * 播放列表适配器
     */
    private var adapter: PlayListAdapter? = null
    private var mLastY: Int = 0
    private var mLastX: Int = 0
    private var deltaX = 0
    private var deltaY = 0

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
        xmVideoView = attachmentControl?.xmVideoView
        activity = attachmentControl?.context as Activity
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
        if (screenH < screenW) {
            val temp = screenH
            screenH = screenW
            screenW = temp
        }
        initDisplay()
        initEvent()
    }

    private fun initDisplay() {
        val mediaPlayer = (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).xmVideoView?.mediaPlayer
        if (mediaPlayer?.isPlaying() == true) {
            ivAction?.setImageResource(pauseResID)
        } else {
            ivAction?.setImageResource(playResID)
        }
    }


    private fun clickBack() {
        BKLog.d(TAG, "点击退出 Landscape -> Portrait")
        // 横屏高度 > 宽度
        //设置竖屏
        ScreenUtil.setPortrait(activity)
        //显示系统状态栏
        ScreenUtil.setDecorVisible(activity)
        //设置宽高
        if (portraitXmVideoViewRect != null) {
            //xmVideoView?.layout(portraitXmVideoViewRect?.left!!, portraitXmVideoViewRect?.top!!, portraitXmVideoViewRect?.right!!, portraitXmVideoViewRect?.bottom!!)
            xmVideoView?.layoutParams?.width = portraitXmVideoViewRect?.right!!
            xmVideoView?.layoutParams?.height = portraitXmVideoViewRect?.bottom!! - ScreenUtil.getStatusBarHeight(activity)

        } else {
            BKLog.e(TAG, "请给portraitXmVideoView属性赋值")
        }
        hideControlView()
        listener?.onState(AttachmentControl.PORTRAIT)
    }

    private fun clickShare() {
        ShareUtil.showShareDlg(activity, this.share?.url, this.share?.title, this.share?.description)
        BKLog.d(TAG, "点击分享")
    }

    private fun clickMore() {
        val xmPopWindow = XmPopWindow(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.attachment_control_landscape_setting_pop, null, false)
        PopSettingViewHolder.create(xmVideoView, view, xmPopWindow)
        xmPopWindow.ini(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        xmPopWindow.showAtLocation(XmPopWindow.Location.RIGHT, R.style.AnimationRightFade, activity?.window?.decorView!!, 0, 0)
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

    private fun progressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        this@LandscapeViewHolder.progress = progress
        if (isHorizontalSlide) {
            //用户在屏幕水平滑动，但未触碰到seekbar时
            BKLog.i(AttachmentControl.TAG, "“未”触碰到Seekbar，滑动中... progress:$progress")
        } else {
            //用户触碰了seekbar或者定时器一直设置progress属性值时
            val pos = ((progress.toFloat() / 100f) * mediaPlayer?.getDuration()!!).toLong()
            updateProgress(pos, false)
            BKLog.i(AttachmentControl.TAG, "触碰到Seekbar，滑动中... progress:$progress")
        }
    }

    private fun startTrackingTouch(seekBar: SeekBar?) {
        showControlView()
        showProgress()
        progressTimerStop()
        progress = 0
        BKLog.d(AttachmentControl.TAG, "开始触发滑动 progress:$progress")
    }

    private fun stopTrackingTouch(seekBar: SeekBar?) {
        hideControlView()
        horizontalSlideStopSeekTo()
        progressTimerStart(period = 1000)
        BKLog.d(AttachmentControl.TAG, "结束触发滑动 progress:$progress")
    }

    private fun clickRatio() {
        val xmPopWindow = XmPopWindow(activity)
        val ratioView = LayoutInflater.from(activity).inflate(R.layout.attachment_control_landscape_ratio, null, false)
        val tvRatio360p: TextView = ratioView.findViewById(R.id.tv_ratio_360p)
        val tvRatio480p: TextView = ratioView.findViewById(R.id.tv_ratio_480p)
        val tvRatio720p: TextView = ratioView.findViewById(R.id.tv_ratio_720p)
        val tvRatio1080p: TextView = ratioView.findViewById(R.id.tv_ratio_1080p)
        tvRatio360p.setOnClickListener {
            Toast.makeText(activity, "360p", Toast.LENGTH_SHORT).show()

            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).ratio(1)
            xmPopWindow.dismiss()
        }
        tvRatio480p.setOnClickListener {
            Toast.makeText(activity, "480p", Toast.LENGTH_SHORT).show()
            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).ratio(2)
            xmPopWindow.dismiss()
        }
        tvRatio720p.setOnClickListener {
            Toast.makeText(activity, "720p", Toast.LENGTH_SHORT).show()
            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).ratio(3)
            xmPopWindow.dismiss()
        }
        tvRatio1080p.setOnClickListener {
            Toast.makeText(activity, "1080p", Toast.LENGTH_SHORT).show()
            (xmVideoView?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl).ratio(3)
            xmPopWindow.dismiss()
        }
        xmPopWindow.ini(ratioView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        xmPopWindow.showAtLocation(XmPopWindow.Location.RIGHT, R.style.AnimationRightFade, activity?.window?.decorView!!, 0, 0)
    }

    @SuppressLint("ClickableViewAccessibility", "ObjectAnimatorBinding")
    private fun initEvent() {
        //顶部
        ivBack?.setOnClickListener {
            clickBack()
        }
        ivShare?.setOnClickListener {
            clickShare()
        }
        ivMore?.setOnClickListener {
            clickMore()
        }

        //底部
        ivAction?.setOnClickListener {
            clickAction()
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
        tvRatio?.setOnClickListener { it ->
            clickRatio()
        }
    }

    override fun setMediaInfo(info: MediaBean) {
        this.mediaInfo = info
    }

    override fun setShareInfo(share: MediaBean.ShareBean) {
        this.share = share
    }

    override fun setTitle(title: String) {
        tvTitle?.text = title
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun hidePlayListAni() {
        //super.hidePlayListAni()
        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!, 0f).setDuration(500).start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun showPlayListAni() {
        //super.showPlayListAni()
        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!.toFloat(), (-rv?.height!!).toFloat()).setDuration(500).start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun hidePlayList() {
        //super.hidePlayList()
        rv?.visibility = View.GONE
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun showPlayList() {
        //super.showPlayList()
        rv?.visibility = View.VISIBLE

    }

    override fun isShowControlView(): Boolean {
        return clLandscapeBottom?.visibility == View.VISIBLE
    }

    override fun showOrHideControlView() {
        if (clLandscapeBottom?.visibility == View.VISIBLE) {
            hideControlView()
        } else {
            showControlView()
        }
    }

    override fun showProgress() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showLoading() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showTop() {
        clLandscapeTop?.visibility = View.VISIBLE
    }

    override fun showBottom() {
        clLandscapeBottom?.visibility = View.VISIBLE
    }

    override fun hideLoading() {

        pbLoading?.visibility = View.GONE
    }

    override fun hideTop() {
        clLandscapeTop?.visibility = View.GONE
    }

    override fun hideBottom() {
        clLandscapeBottom?.visibility = View.GONE
    }

    override fun hideProgress() {
        pbLoading?.visibility = View.GONE
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

    override fun horizontalSlideStopSeekTo() {
        val seekPos = mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    override fun setActionResID(id: Int) {
        try {
            ivAction?.setImageResource(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun secondaryProgress(present: Int) {
        seekBar?.secondaryProgress = present
    }

    override fun progress(present: Int) {
        seekBar?.progress = present
    }

    override fun setPlayList(listener: OnPlayListItemClickListener?) {
        if (mediaInfo != null && !mediaInfo?.mediaInfos?.isEmpty()!!) {
            activity?.runOnUiThread {
                val l = LinearLayoutManager(activity)
                l.orientation = LinearLayoutManager.HORIZONTAL
                rv?.layoutManager = l
                adapter = PlayListAdapter(mediaInfo?.mediaInfos, xmVideoView, listener)
                rv?.adapter = adapter
            }
        } else {
            BKLog.e("设置列表失败")
        }
    }

    override fun selectPlayList(index: Int) {
        for (item in adapter?.data!!) {
            item.select = false
        }
        hideBottom()
        hidePlayList()
        hidePlayListAni()
        adapter?.data!![index].select = true
        adapter?.notifyDataSetChanged()
    }

    override fun setShareListener(listener: View.OnClickListener) {
        ivShare?.setOnClickListener(listener)
    }

    @Deprecated("")
    fun list() {
    }


    @Deprecated("可以不用了")
    fun setPlayList(courseDetail: CoursesDetailCBean?) {
    }
}
