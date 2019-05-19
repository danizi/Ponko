package com.ponko.cn.api

import com.ponko.cn.bean.SearchCBean
import com.ponko.cn.bean.SearchRecordCBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    /**
     * 搜索关键字存入数据库中只保存十条记录
     */
    /**
     * 搜索接口
     */
    @GET("course/search")
    fun search(@Query("q") key: String): Call<SearchCBean>

    /**
     * 搜索窗口里面的观看记录
     */
    @GET("course/recent_watched")
    fun searchRecord(): Call<SearchRecordCBean>
}