package com.ponko.cn.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by 取个佛系名字：如去 on 2017/12/31.
 * 课程详情
 */
class CourseInfo : Serializable {

    var id: String? = null

    var title: String? = null

    var features: String? = null
    @SerializedName("share_base_url")
    var shareBaseUrl: String? = null

    var image: String? = null

    @SerializedName("possess")
    var isPossess = false //是否拥有

    @SerializedName("ppt")
    var hasPPT = false //是否有PPT

    //var ppts: ArrayList<PPT>? = null


    var chapters: ArrayList<Chapter>? = null


    var playing: String? = null

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("pay_url")
    var payUrl: String? = null


}