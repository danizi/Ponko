package com.ponko.cn.module.media.control.viewholder.landscape

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ponko.cn.bean.MediaBean
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.R
import com.xm.lib.media.attachment.OnPlayListItemClickListener
import com.xm.lib.media.base.XmVideoView

/**
 * 播放列表Adapter
 */
class PlayListAdapter(val data: List<MediaBean.MediaInfo>?, val xmVideoView: XmVideoView?, val listener: OnPlayListItemClickListener?) : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        BKLog.d("onCreateViewHolder viewType:$viewType")
        return PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return if (data?.isEmpty()!!) {
            0
        } else {
            data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(data!![position], xmVideoView, position, listener)
        BKLog.d("onBindViewHolder position:$position")
    }
}
