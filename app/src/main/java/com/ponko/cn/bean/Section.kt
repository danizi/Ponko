package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by 取个佛系名字：如去 on 2017/12/31.
 * 课件小节数组
 */
class Section() : Parcelable, Serializable {

    var id: String? = null
    @SerializedName("name")
    var sectionName: String? = null
    var free = false

    var avatar: String? = null //小节缩列图

    @SerializedName("completed_status")
    var completedStatus = 0


    var vid: String? = null //保利威视播放视频vid

    @SerializedName("progress_duration")
    var progress: String? = null //上次观看到的时长


    var duration = 0L //上次观看到的时长

    var courseId: String? = null

    var isChecked = false
    var isSelector = false

    var collection = false

    var sectionCount = 0

    var chapterName: String? = null
    var chapterImage: String? = null

    var isLocal = false
//    var fileSize = 0L
//
//    var filePath = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        sectionName = parcel.readString()
        free = parcel.readByte() != 0.toByte()
        avatar = parcel.readString()
        chapterImage = parcel.readString()
        chapterName = parcel.readString()
        completedStatus = parcel.readInt()
        vid = parcel.readString()
        progress = parcel.readString()
        courseId = parcel.readString()
        duration = parcel.readLong()
        sectionCount = parcel.readInt()
        isChecked = parcel.readByte() != 0.toByte()
        isSelector = parcel.readByte() != 0.toByte()
        collection = parcel.readByte() != 0.toByte()
        isLocal = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(sectionName)
        parcel.writeByte(if (free) 1 else 0)
        parcel.writeString(avatar)
        parcel.writeInt(completedStatus)
        parcel.writeInt(sectionCount)
        parcel.writeString(vid)
        parcel.writeString(progress)
        parcel.writeString(courseId)
        parcel.writeString(chapterName)
        parcel.writeString(chapterImage)
        parcel.writeLong(duration)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeByte(if (isSelector) 1 else 0)
        parcel.writeByte(if (collection) 1 else 0)
        parcel.writeByte(if (isLocal) 1 else 0)

    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Section> {
        override fun createFromParcel(parcel: Parcel): Section = Section(parcel)

        override fun newArray(size: Int): Array<Section?> = arrayOfNulls(size)
    }


}