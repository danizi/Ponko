package com.ponko.cn.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by 取个佛系名字：如去 on 2017/12/28.
 * 广告bean
 */
class AdBean: Serializable {
    /**
     * image : http://cdn.tradestudy.cn/upload/product/ad/ad1@3x.png
     * width : 1125
     * id : 2fd61be6e62d11e7936c305a3a522e0b
     * type : URL
     * courseId : null
     * url : http://www.tradestudy.cn/teawea
     * height : 390
     */

    var image: String? = null
    var width: Int = 0
    var id: String? = null
    var type: String? = null
    var courseId: String? = null
    var url: String? = null
    var title: String? = null
    var height: Int = 0
    @SerializedName("link_value")
    var link: String? = null

}