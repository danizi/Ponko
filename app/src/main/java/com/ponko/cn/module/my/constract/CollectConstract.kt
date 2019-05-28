package com.ponko.cn.module.my.constract

import android.content.Context

/**
 * 收藏契约类 - MVP模式
 */
class CollectConstract {
    /**
     * 视图层
     */
    interface V{}

    /**
     * 数据层
     */
    class M{}

    /**
     * 控制层
     */
    class Present(context:Context,v:V){
        private val m = M()

    }
}