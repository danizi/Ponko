package com.ponko.cn.module.my.option.acount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ponko.cn.R

class PersonalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)
    }

    private class ItemBean(var content: HashMap<String, String>)
}
