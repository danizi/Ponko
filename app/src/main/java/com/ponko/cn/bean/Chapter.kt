package com.ponko.cn.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by 取个佛系名字：如去 on 2017/12/31.
 * 章节
 */
class Chapter() : Parcelable ,Serializable{

    var id: String? = null

    @SerializedName("chapter_name")
    var chapterName: String? = null

    var sections:List<Section>? = null


    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        chapterName = parcel.readString()
        sections = parcel.createTypedArrayList(Section)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(chapterName)
        parcel.writeTypedList(sections)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Chapter> {
        override fun createFromParcel(parcel: Parcel): Chapter = Chapter(parcel)

        override fun newArray(size: Int): Array<Chapter?> = arrayOfNulls(size)
    }
}