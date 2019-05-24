package com.ponko.cn.db.dao

import android.database.sqlite.SQLiteDatabase
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.bean.CourseSpecialDbBean
import com.xm.lib.common.log.BKLog

/**
 * 专题操作类数据库类
 */
class CourseSpecialDao(private var db: SQLiteDatabase?) {

    fun insert(bean: CourseSpecialDbBean) {
        if (db?.isOpen == true) {
            if(select(bean).isEmpty()){
                db?.execSQL(CacheContract.CourseSpecialTable.SQL_INSERT, arrayOf(
                        bean.uid,
                        bean.special_id,
                        bean.teacher,
                        bean.num,
                        bean.cover,
                        bean.title
                ))
                //db?.close()
            }else{
                BKLog.d("专题已存在")
            }
        } else {
            BKLog.d("数据库未打开")
        }
    }

    fun update(bean: CourseSpecialDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseSpecialTable.SQL_UPDATE_BY_ID, arrayOf(
                    bean.uid,
                    bean.special_id,
                    bean.teacher,
                    bean.num,
                    bean.cover,
                    bean.title,
                    bean.special_id))
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }

    fun delete(bean: CourseSpecialDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseSpecialTable.SQL_DEL_BY_ID, arrayOf(bean.special_id))
            //db?.close()
        }
    }

    fun deleteAll() {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseSpecialTable.SQL_DEL_All, null)
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }

    }

    fun select(bean: CourseSpecialDbBean): ArrayList<CourseSpecialDbBean> {
        val queryData = ArrayList<CourseSpecialDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseSpecialTable.SQL_SELECT_BY_ID, arrayOf(bean.special_id))
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
            }
            while (cursor?.moveToNext()!!) {
                val courseSpecialDbBean = CourseSpecialDbBean()
                val id = cursor.getString(0)
                courseSpecialDbBean.uid = cursor.getString(1)
                courseSpecialDbBean.special_id = cursor.getString(2)
                courseSpecialDbBean.teacher = cursor.getString(3)
                courseSpecialDbBean.num = cursor.getInt(4)
                courseSpecialDbBean.cover = cursor.getString(5)
                courseSpecialDbBean.title = cursor.getString(6)
                queryData.add(courseSpecialDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        return queryData
    }

    fun select(specialId: String): ArrayList<CourseSpecialDbBean> {
        val queryData = ArrayList<CourseSpecialDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseSpecialTable.SQL_SELECT_BY_ID, arrayOf(specialId))
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
            }
            while (cursor?.moveToNext()!!) {
                val courseSpecialDbBean = CourseSpecialDbBean()
                val id = cursor.getString(0)
                courseSpecialDbBean.uid = cursor.getString(1)
                courseSpecialDbBean.special_id = cursor.getString(2)
                courseSpecialDbBean.teacher = cursor.getString(3)
                courseSpecialDbBean.num = cursor.getInt(4)
                courseSpecialDbBean.cover = cursor.getString(5)
                courseSpecialDbBean.title = cursor.getString(6)
                queryData.add(courseSpecialDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        return queryData
    }

    fun selectAll(): ArrayList<CourseSpecialDbBean> {
        val queryAllData = ArrayList<CourseSpecialDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseSpecialTable.SQL_SELECT_ALL, null)
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
                return queryAllData
            }
            while (cursor.moveToNext()) {
                val courseSpecialDbBean = CourseSpecialDbBean()
                val id = cursor.getString(0)
                courseSpecialDbBean.uid = cursor.getString(1)
                courseSpecialDbBean.special_id = cursor.getString(2)
                courseSpecialDbBean.teacher = cursor.getString(3)
                courseSpecialDbBean.num = cursor.getInt(4)
                courseSpecialDbBean.cover = cursor.getString(5)
                courseSpecialDbBean.title = cursor.getString(6)
                queryAllData.add(courseSpecialDbBean)
            }
        } else {
            BKLog.d("数据库未打开")
        }
        return queryAllData
    }

    /**
     * 判断是否存在
     */
    fun exist(){

    }
}