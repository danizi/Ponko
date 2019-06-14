package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable

class RemindCBean() : Parcelable {


    /**
     * createTime : 1513842467260
     * unread : true
     * title : 标题
     * description : 12323213213
     * id : 08536c17e62311e7936c305a3a522e0b
     * type : URL
     * url : http://www.baidu.com
     */

    var createTime: Long = 0
    var unread: Boolean = false
    var title: String? = null
    var description: String? = null
    var id: String? = null
    var type: String? = null
    var url: String? = null

    constructor(parcel: Parcel) : this() {
        createTime = parcel.readLong()
        unread = parcel.readByte() != 0.toByte()
        title = parcel.readString()
        description = parcel.readString()
        id = parcel.readString()
        type = parcel.readString()
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(createTime)
        parcel.writeByte(if (unread) 1 else 0)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(type)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemindCBean> {
        override fun createFromParcel(parcel: Parcel): RemindCBean {
            return RemindCBean(parcel)
        }

        override fun newArray(size: Int): Array<RemindCBean?> {
            return arrayOfNulls(size)
        }
    }
}