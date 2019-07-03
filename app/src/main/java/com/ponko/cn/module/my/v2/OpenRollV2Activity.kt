package com.ponko.cn.module.my.v2

import android.content.Context
import android.content.Intent
import com.ponko.cn.module.web.WebAct
class OpenRollV2Activity : WebAct(){
    companion object {
        fun start(context: Context?, link_type: String?, link_value: String?, title: String? = "", lunMode: Int? = Intent.FLAG_ACTIVITY_SINGLE_TOP) {
            WebAct.start(context,link_type,link_value,title,lunMode)
        }
    }
}
