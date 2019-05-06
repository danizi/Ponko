package com.ponko.cn.bean

/**
 * RecyclerView 绑定item的实体类
 */
class BindItemViewHolderBean private constructor(val viewType: Array<Int>, val viewHolderClass: Array<Class<*>>, val beanClass: Array<Class<*>>, val itemViewLayoutId: Array<Int>) {
    companion object {
        fun create(viewType: Array<Int>, viewHolderClass: Array<Class<*>>, beanClass: Array<Class<*>>, itemViewLayoutId: Array<Int>): BindItemViewHolderBean {
            return BindItemViewHolderBean(viewType, viewHolderClass, beanClass, itemViewLayoutId)
        }
    }
}
