package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class RecordCBean() : Parcelable {

    @SerializedName("today")//几天的数据
    var recordWithToday: MutableList<HistoryRecordBean>? = null
    @SerializedName("earlier")//往日的数据
    var recordWithEarlier: MutableList<HistoryRecordBean>? = null

    constructor(parcel: Parcel) : this() {
        recordWithToday = parcel.createTypedArrayList(HistoryRecordBean)
        recordWithEarlier = parcel.createTypedArrayList(HistoryRecordBean)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecordCBean> {
        override fun createFromParcel(parcel: Parcel): RecordCBean {
            return RecordCBean(parcel)
        }

        override fun newArray(size: Int): Array<RecordCBean?> {
            return arrayOfNulls(size)
        }
    }

    class HistoryRecordBean() : Parcelable {

        var sectionName: String? = null
        var id: String? = null
        var position = 0
        var completed: String? = null
        var sectionId: String? = null
        var avatar: String? = null
        var courseId: String? = null
        var durationForSecond = 0
        var isToday = false

        constructor(parcel: Parcel) : this() {
            sectionName = parcel.readString()
            id = parcel.readString()
            position = parcel.readInt()
            completed = parcel.readString()
            sectionId = parcel.readString()
            avatar = parcel.readString()
            courseId = parcel.readString()
            durationForSecond = parcel.readInt()
            isToday = parcel.readByte() != 0.toByte()
            parcel.writeByte(if (isToday) 1 else 0)
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(sectionName)
            parcel.writeString(id)
            parcel.writeInt(position)
            parcel.writeString(completed)
            parcel.writeString(sectionId)
            parcel.writeString(avatar)
            parcel.writeString(courseId)
            parcel.writeInt(durationForSecond)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<HistoryRecordBean> {
            override fun createFromParcel(parcel: Parcel): HistoryRecordBean {
                return HistoryRecordBean(parcel)
            }

            override fun newArray(size: Int): Array<HistoryRecordBean?> {
                return arrayOfNulls(size)
            }
        }

    }

}