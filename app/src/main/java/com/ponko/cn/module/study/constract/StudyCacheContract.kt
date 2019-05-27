package com.ponko.cn.module.study.constract

import android.content.Context

/**
 * 课程学习详情，下载页面契约类 - MVP模式
 */
class StudyCacheContract {
    /**
     * 视图层
     */
    interface V {}

    /**
     * 数据层
     */
    class M {}

    /**
     * 控制层
     */
    class Present(context: Context, v: V) {

    }
}