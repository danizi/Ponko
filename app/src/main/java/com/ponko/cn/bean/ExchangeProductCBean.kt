package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable

class ExchangeProductCBean() : Parcelable {


    /**
     * scores : 1000
     * createTime : 1520847914000
     * iamge :
     * name : 1年有效期兑换券
     * express :
     * id : 109052b725da11e88cc60242ac130003
     * postid :
     * send : true
     * entity : true
     */

    var scores: Int = 0
    var createTime: Long = 0
    var iamge: String? = null
    var name: String? = null
    var express: String? = null
    var id: String? = null
    var postid: String? = null
    var isSend: Boolean = false
    var isEntity: Boolean = false

    constructor(parcel: Parcel) : this() {
        scores = parcel.readInt()
        createTime = parcel.readLong()
        iamge = parcel.readString()
        name = parcel.readString()
        express = parcel.readString()
        id = parcel.readString()
        postid = parcel.readString()
        isSend = parcel.readByte() != 0.toByte()
        isEntity = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(scores)
        parcel.writeLong(createTime)
        parcel.writeString(iamge)
        parcel.writeString(name)
        parcel.writeString(express)
        parcel.writeString(id)
        parcel.writeString(postid)
        parcel.writeByte(if (isSend) 1 else 0)
        parcel.writeByte(if (isEntity) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ExchangeProductCBean> {
        override fun createFromParcel(parcel: Parcel): ExchangeProductCBean =
                ExchangeProductCBean(parcel)

        override fun newArray(size: Int): Array<ExchangeProductCBean?> = arrayOfNulls(size)

    }
}