package com.ponko.cn.http

import android.text.TextUtils
import com.xm.lib.common.http.RetrofitClient
import com.xm.lib.common.log.BKLog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

abstract class HttpCallBack<T> : RetrofitClient.BaseCallback<T>() {
    private val TAG = "HttpCallBack"

    override fun onFailure(call: Call<T>?, msg: String?) {

    }

    override fun errorMsg(response: Response<T>?) {
        val errorString = response?.errorBody()?.string()
        if (TextUtils.isEmpty(errorString)) {
            BKLog.e(TAG, "errorString is empty")
            onFailure(null, "")
            return
        }
        val jsonObject = JSONObject(errorString)
        val code = jsonObject.get("code") as String
        val url = response?.raw()?.request()?.url().toString()
        val msg = httpErrorCodeMaps(url)[code]
        BKLog.d(TAG, "url ->$url code -> $code msg ->$msg ")
    }

    private fun httpErrorCodeMaps(url: String): HashMap<String, String> {
        val errorCodeMaps = HashMap<String, String>()
        errorCodeMaps[""] = ""
        return errorCodeMaps
    }
}