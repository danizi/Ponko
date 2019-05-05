package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by 取个佛系名字：如去 on 2018/1/31.
 */
class AddressBean() : Parcelable {

    var id :String?=null
    var tel :String?=null
    var recipient :String?=null
    var address :String?=null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        tel = parcel.readString()
        recipient = parcel.readString()
        address = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(tel)
        parcel.writeString(recipient)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddressBean> {
        override fun createFromParcel(parcel: Parcel): AddressBean {
            return AddressBean(parcel)
        }

        override fun newArray(size: Int): Array<AddressBean?> {
            return arrayOfNulls(size)
        }
    }
}