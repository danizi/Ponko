package com.ponko.cn.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object MultipartUtil {
    fun convertToBody(file: File): RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)

    fun convertToPart(key: String, file: File): MultipartBody.Part {
        val imageBody = convertToBody(file)
        return MultipartBody.Part.createFormData(key, file.name, imageBody)
    }
}