package com.ponko.cn.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xm.lib.common.log.BKLog

/**
 * 应用数据库
 */
class PonkoDBHelp(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    /**
     * 数据库第一次创建会调用此函数
     */
    override fun onCreate(db: SQLiteDatabase?) {
        BKLog.d("onCreate 创建表")
        db?.execSQL(CacheContract.CourseSpecialTable.SQL_CREATE_COURSE_SPECIAL_TABLE)
        db?.execSQL(CacheContract.CourseTable.SQL_CREATE_TABLE)
        db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_CREATE_COURSE_COLLECT_SPECIAL_TABLE)
        db?.execSQL(CacheContract.CourseCollectSectionTable.SQL_CREATE_COURSE_COLLECT_SECTION_TABLE)
    }

    /**
     * 版本更新调用此函数
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        BKLog.d("onUpgrade 数据库升级，oldVersion$oldVersion newVersion$newVersion")
    }
}