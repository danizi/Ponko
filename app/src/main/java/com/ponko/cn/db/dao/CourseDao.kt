package com.ponko.cn.db.dao

import android.database.sqlite.SQLiteDatabase
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.bean.CourseDbBean
import com.xm.lib.common.log.BKLog

class CourseDao(private var db: SQLiteDatabase?) {

    fun insert(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            if (select(bean).isEmpty()) {
                db?.execSQL(CacheContract.CourseTable.SQL_INSERT, arrayOf(
                        bean.column_uid,
                        bean.column_special_id,
                        bean.column_course_id,
                        bean.column_cover,
                        bean.column_title,
                        bean.column_total,
                        bean.column_progress,
                        bean.column_complete,
                        bean.column_m3u8_url,
                        bean.column_key_ts_url,
                        bean.column_down_path
                ))
                //db?.close()
            } else {
                BKLog.d("课程已存在")
            }
        } else {
            BKLog.d("数据库未打开")
        }
    }

    fun update(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseSpecialTable.SQL_UPDATE_BY_ID, arrayOf(
                    bean.column_uid,
                    bean.column_special_id,
                    bean.column_course_id,
                    bean.column_cover,
                    bean.column_title,
                    bean.column_total,
                    bean.column_progress,
                    bean.column_complete,
                    bean.column_m3u8_url,
                    bean.column_key_ts_url,
                    bean.column_down_path))
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }

    fun delete(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_DEL_BY_ID, arrayOf(bean.column_special_id))
            //db?.close()
        }
    }

    fun deleteAll() {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_DEL_ALL, null)
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }

    fun select(bean: CourseDbBean): ArrayList<CourseDbBean> {
        val queryData = ArrayList<CourseDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseTable.SQL_SELECT_BY_ID, arrayOf(bean.column_special_id))
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
            }
            while (cursor?.moveToNext()!!) {
                val courseDbBean = CourseDbBean()
                val id = cursor.getString(0)
                courseDbBean.column_uid = cursor.getString(1)
                courseDbBean.column_special_id = cursor.getString(2)
                courseDbBean.column_course_id = cursor.getString(3)
                courseDbBean.column_cover = cursor.getString(4)
                courseDbBean.column_title = cursor.getString(5)
                courseDbBean.column_total = cursor.getInt(6)
                courseDbBean.column_progress = cursor.getInt(7)
                courseDbBean.column_complete = cursor.getInt(8)
                courseDbBean.column_m3u8_url = cursor.getString(9)
                courseDbBean.column_key_ts_url = cursor.getString(10)
                courseDbBean.column_down_path = cursor.getString(11)
                queryData.add(courseDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        return queryData
    }

    fun selectAll(): ArrayList<CourseDbBean> {
        val queryAllData = ArrayList<CourseDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseTable.SQL_SELECT_ALL, null)
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
            }
            while (cursor?.moveToNext()!!) {
                val courseDbBean = CourseDbBean()
                courseDbBean.column_uid = cursor.getString(1)
                courseDbBean.column_special_id = cursor.getString(2)
                courseDbBean.column_course_id = cursor.getString(3)
                courseDbBean.column_cover = cursor.getString(4)
                courseDbBean.column_title = cursor.getString(5)
                courseDbBean.column_total = cursor.getInt(6)
                courseDbBean.column_progress = cursor.getInt(7)
                courseDbBean.column_complete = cursor.getInt(8)
                courseDbBean.column_m3u8_url = cursor.getString(9)
                courseDbBean.column_key_ts_url = cursor.getString(10)
                courseDbBean.column_down_path = cursor.getString(11)
                queryAllData.add(courseDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        return queryAllData
    }
}