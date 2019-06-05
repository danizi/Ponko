package com.ponko.cn.module.free

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.ponko.cn.R
import com.ponko.cn.app.PonkoApp
import com.ponko.cn.bean.DetailCBean
import com.ponko.cn.bean.DetailTopBean
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.module.common.PonkoBaseAct
import com.ponko.cn.module.free.adapter.FreeDetailFragmentPagerAdapter
import com.ponko.cn.module.free.constract.FreeDetailsConstract
import com.ponko.cn.module.media.AttachmentPre
import com.ponko.cn.module.media.MediaUitl
import com.ponko.cn.module.media.control.AttachmentControl
import com.ponko.cn.utils.DialogUtil
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.XmStateView
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.base.XmVideoView


/**
 * 免费详情页面
 */
class FreeDetailsAct : PonkoBaseAct<FreeDetailsConstract.Present>(), FreeDetailsConstract.V {

    companion object {
        const val TAG = "FreeDetailsAct"
        /**
         * 进入免费详情页面
         * @param context 上下文对象
         * @param id      课程id
         */
        fun start(context: Context, id: String) {
            val intent = Intent(context, FreeDetailsAct::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    /**
     * 窗口ui相关控件
     */
    private var ui: ViewHolder? = null

    /**
     * 附着播放器控制器
     */
    private var attachmentControl: AttachmentControl? = null

    /**
     * 免费详情反馈实体
     */
    private var detailCBean: DetailCBean? = null

    override fun setContentViewBefore() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        p?.getIntentExtras(intent)
        super.onCreate(savedInstanceState)
        //注册item点击广播
        p?.registerClickFreeItemReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消item点击广播
        p?.unRegisterClickFreeItemReceiver()
        ui?.video?.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        //播放器暂停
        ui?.video?.onPause()
    }

    override fun presenter(): FreeDetailsConstract.Present {
        return FreeDetailsConstract.Present(context = this, v = this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_free_details
    }

    override fun findViews() {
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
    }

    override fun initDisplay() {
        //系统栏颜色
        com.jaeger.library.StatusBarUtil.setColor(this, this.resources.getColor(R.color.black), 0)

        //初始化播放器View附着页面
        MediaUitl.initXmVideoView(ui?.video!!, this)
        attachmentControl = ui?.video?.attachmentViewMaps!!["AttachmentControl"] as AttachmentControl

        //动态设置播放器View的大小，适配
        setVideoSize()
    }

    private fun setVideoSize() {
        //按比例设置播放器View
        val present = 1f / 1.8f
        val layoutParams = ui?.video?.layoutParams
        layoutParams?.height = ((ScreenUtil.getNormalWH(this)[0]) * present).toInt()
        ui?.video?.layoutParams = layoutParams
    }

    override fun iniData() {
        //获取intent携带信息
        p?.getIntentExtras(intent)
        //显示加载框
        DialogUtil.showProcess(this)
        //请求免费页面详情接口
        p?.requestFreeDetailApi()
    }

    override fun requestFreeDetailApiSuccess(detailCBean: DetailCBean?) {
        DialogUtil.hideProcess()
        ui?.xmStateView?.hide()
    }

    override fun requestFreeDetailApiFailure() {
        ui?.xmStateView?.showError("请求数据失败", View.OnClickListener {
            p?.requestFreeDetailApi()
        })
        DialogUtil.hideProcess()
    }

    override fun iniEvent() {
        ui?.llShareFriend?.setOnClickListener {
            p?.clickShareFriend()
        }
        ui?.llShareWx?.setOnClickListener {
            p?.clickShareWx()
        }
        ui?.btnPay?.setOnClickListener {
            p?.clickPay()

        }
        ui?.vp?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                //滑动时再重新计算viewpager的大小
                ui?.vp?.requestLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        attachmentControl?.setOnPlayListItemClickListener(object : OnPlayListItemClickListener {
            override fun item(vid: String?, progress: Int?, view: View?, postion: Int) {
                //更新下竖屏列表item
                p?.clickPlayListItem(vid, progress, view, postion)

                //暂停播放
                attachmentControl?.pause()
                attachmentControl?.showLoading()

                //更新下横屏列表item
                attachmentControl?.updateListItem(postion)

                //设置标题
                attachmentControl?.setTitle(detailCBean?.title!!)

                //播放视频
                attachmentControl?.start(vid!!, progress, postion)
            }
        })
    }

    override fun showPlayLoading() {
        attachmentControl?.showLoading()
    }

    override fun play(vid: String?) {
        attachmentControl?.start(vid!!, 0, 0)
    }

    override fun setMediaInfo(buildPlayListByFree: MediaBean) {
        //设置播放列表信息
        attachmentControl?.setMediaInfo(buildPlayListByFree)
    }

    @SuppressLint("SetTextI18n")
    override fun displayTop(detailTopBean: DetailTopBean?) {
        ui?.tvTeacher?.text = "${detailTopBean?.teacher}老师"
        ui?.tvClass?.text = "${detailTopBean?.count}节课"
        ui?.tvTime?.text = "${detailTopBean?.duration!!}分钟"
    }

    override fun displayVideo(detailCBean: DetailCBean?) {
        this.detailCBean = detailCBean
        val attachmentPre = ui?.video?.getChildAt(0) as AttachmentPre
        val vid = detailCBean?.chapters!![0].sections[0].vid
        val preUrl = detailCBean.chapters!![0].sections[0]?.avatar
        val isB2BVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![0].isIs_vip)
        val isB2CVip = (!PonkoApp.mainCBean?.types?.isEmpty()!! && PonkoApp.mainCBean?.types!![1].isIs_vip)
        val isPay = detailCBean.chapters!![0].sections[0].isFree || isB2BVip || isB2CVip
        attachmentPre.load(
                vid = vid,
                preUrl = preUrl,
                isPay = isPay)
    }

    override fun displayContent(frgs: ArrayList<Fragment>, titles: ArrayList<String>) {
        ui?.vp?.adapter = FreeDetailFragmentPagerAdapter(frgs, titles, supportFragmentManager)
        ui?.tb?.setupWithViewPager(ui?.vp)
    }

    /**
     * 免费页面窗口ui ViewHolder
     */
    private class ViewHolder private constructor(val video: XmVideoView, val tvCourseTitle: TextView, val llDes: LinearLayout, val tvTeacher: TextView, val tvClass: TextView, val tvTime: TextView, val btnPay: Button, val llShareWx: LinearLayout, val llShareFriend: LinearLayout, val tb: TabLayout, val vp: ViewPager, val xmStateView: XmStateView) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val video = rootView.findViewById<View>(R.id.video) as XmVideoView
                val tvCourseTitle = rootView.findViewById<View>(R.id.tv_course_title) as TextView
                val llDes = rootView.findViewById<View>(R.id.ll_des) as LinearLayout
                val tvTeacher = rootView.findViewById<View>(R.id.tv_teacher) as TextView
                val tvClass = rootView.findViewById<View>(R.id.tv_class) as TextView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val btnPay = rootView.findViewById<View>(R.id.btn_pay) as Button
                val llShareWx = rootView.findViewById<View>(R.id.ll_share_wx) as LinearLayout
                val llShareFriend = rootView.findViewById<View>(R.id.ll_share_friend) as LinearLayout
                val tb = rootView.findViewById<View>(R.id.tb) as TabLayout
                val vp = rootView.findViewById<View>(R.id.vp) as ViewPager
                val viewState = rootView.findViewById<View>(R.id.view_state) as XmStateView
                return ViewHolder(video, tvCourseTitle, llDes, tvTeacher, tvClass, tvTime, btnPay, llShareWx, llShareFriend, tb, vp, viewState)
            }
        }
    }

}
