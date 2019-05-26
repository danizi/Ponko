package com.ponko.cn.module.study.constract

import android.content.Context

/**
 * 课程学习详情页面契约类 - MVP模式
 */
class StudyCourseDetailContract {
    /**
     * UI层
     */
    interface V {}

    /**
     * 数据层
     */
    class M {}

    /**
     * 控制层
     * @param context 上下文对象
     * @param v UI层
     */
    class Present(context: Context, v: V) {
        private val m = M()

    }
}