package com.ponko.cn.api

import com.ponko.cn.bean.MainCBean
import retrofit2.Call
import retrofit2.http.GET

interface StudyApi {
    @GET("main")
    fun main(): Call<MainCBean>
}