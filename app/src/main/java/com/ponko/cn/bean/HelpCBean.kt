package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable

class HelpCBean() : Parcelable {

    var id: String? = null
    var title: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<HelpCBean> {
        override fun createFromParcel(parcel: Parcel): HelpCBean = HelpCBean(parcel)

        override fun newArray(size: Int): Array<HelpCBean?> = arrayOfNulls(size)
    }
}