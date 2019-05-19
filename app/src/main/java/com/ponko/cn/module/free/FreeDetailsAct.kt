package com.ponko.cn.module.free

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R

class FreeDetailsAct : AppCompatActivity() {
    companion object {
        fun start(context: Context, id: String) {
            val intent = Intent(context, FreeDetailsAct::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_details)
    }
}
