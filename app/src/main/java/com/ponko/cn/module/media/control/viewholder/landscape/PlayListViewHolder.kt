package com.ponko.cn.module.media.control.viewholder.landscape

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ponko.cn.bean.MediaBean
import com.ponko.cn.utils.ToastUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.base.XmVideoView

/**
 * 播放列表ViewHolder
 */
class PlayListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var cover: ImageView? = null
    private var title: TextView? = null
    private var tvChapter: TextView? = null
    fun bind(any: MediaBean.MediaInfo, mediaPlayer: XmVideoView?, position: Int, listener: OnPlayListItemClickListener?) {
        val sectionsBean = any
        cover = itemView.findViewById(R.id.iv_cover)
        title = itemView.findViewById(R.id.tv_title)
        tvChapter = itemView.findViewById(R.id.tv_chapter)
        com.ponko.cn.utils.Glide.with(itemView.context, sectionsBean.avatar, cover)
        title?.text = sectionsBean.name
        if (sectionsBean.select == true) {
            title?.setTextColor(Color.parseColor("#FF5A5E"))
            tvChapter?.setTextColor(Color.parseColor("#FF5A5E"))
        } else {
            title?.setTextColor(Color.parseColor("#ffffff"))
            tvChapter?.setTextColor(Color.parseColor("#ffffff"))
            sectionsBean.select = false
        }
        itemView.setOnClickListener {
            if (sectionsBean.isPay!! || sectionsBean.isFree!!) {

                listener?.item(sectionsBean.vid, sectionsBean.progress_duration, itemView, position)
            } else {
                ToastUtil.show("请先购买")
            }
        }
    }
}