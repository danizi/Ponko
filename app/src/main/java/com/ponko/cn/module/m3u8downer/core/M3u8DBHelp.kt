package com.ponko.cn.module.m3u8downer.core

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xm.lib.common.log.BKLog

/**
 * 应用数据库
 */
class M3u8DBHelp(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    /**
     * 数据库第一次创建会调用此函数
     */
    override fun onCreate(db: SQLiteDatabase?) {
        BKLog.d("onCreate 下载器创建表")
        db?.execSQL(M3u8DbContract.Table.SQL_CREATE_TABLE)
    }

    /**
     * 版本更新调用此函数
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        BKLog.d("onUpgrade 数据库升级，oldVersion$oldVersion newVersion$newVersion")
    }
}