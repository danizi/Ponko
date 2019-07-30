package com.ponko.cn.db.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.bean.CourseCollectSpecialDbBean
import com.xm.lib.common.log.BKLog

/**
 * 收藏-专题Dao
 */
class CourseCollectSpecialDao(private var db: SQLiteDatabase?) {

    companion object {
        private const val TAG = "CourseCollectSpecialDao"
    }

    /**
     * 插入数据
     */
    fun insert(bean: CourseCollectSpecialDbBean) {
        if (!isOpen("数据插入失败")) return
        if (!isSelect("插入数据", bean.column_course_id).isEmpty()) return
        db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_INSERT, arrayOf(
                bean.column_uid,
                bean.column_course_id,
                bean.column_teacher,
                bean.column_num,
                bean.column_cover,
                bean.column_title
        ))
    }

    /**
     * 删除
     */
    fun deleteByCourseId(courseId: String) {
        if (!isOpen("删除数据失败")) return
        if (isSelect("未删除数据", courseId).isEmpty()) return
        val courseCollectSectionDbBeans = selectByCourseId(courseId)
        for (bean in courseCollectSectionDbBeans) {
            db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_DETELE_BY_COURSE_ID, arrayOf(
                    bean.column_course_id
            ))
        }
    }

    /**
     * 更新
     */
    fun updateByCourseId(courseId: String, bean: CourseCollectSpecialDbBean) {
        if (!isOpen("更新数据失败")) return
        if (isSelect("未更新数据", courseId).isEmpty()) return
        db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_UPDATE_BY_COURSE_ID, arrayOf(
                bean.column_uid,
                bean.column_course_id,
                bean.column_teacher,
                bean.column_num,
                bean.column_cover,
                bean.column_title, bean.column_course_id
        ))
    }

    /**
     * 查找
     */
    fun selectByCourseId(courseId: String): ArrayList<CourseCollectSpecialDbBean> {
        val data = ArrayList<CourseCollectSpecialDbBean>()
        var cursor: Cursor? = null
        try {
             cursor = db?.rawQuery(CacheContract.CourseCollectSpecialTable.SQL_SELECT_BY_COURSE_ID, arrayOf(courseId))
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val courseCollectSpecialDbBean = CourseCollectSpecialDbBean()
                    cursor.getString(0) //主键值
                    courseCollectSpecialDbBean.column_uid = cursor.getString(1)
                    courseCollectSpecialDbBean.column_course_id = cursor.getString(2)
                    courseCollectSpecialDbBean.column_teacher = cursor.getString(3)
                    courseCollectSpecialDbBean.column_num = cursor.getString(4)
                    courseCollectSpecialDbBean.column_cover = cursor.getString(5)
                    courseCollectSpecialDbBean.column_title = cursor.getString(6)
                    data.add(courseCollectSpecialDbBean)
                }
            } else {
                BKLog.e(TAG, "cursor is null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return data
    }

    /**
     * 查找所有
     */
    fun selectAll(): ArrayList<CourseCollectSpecialDbBean> {
        val data = ArrayList<CourseCollectSpecialDbBean>()
        var cursor:Cursor? = null

        try {
             cursor = db?.rawQuery(CacheContract.CourseCollectSpecialTable.SQL_SELECT_BY_ALL, arrayOf())
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val courseCollectSpecialDbBean = CourseCollectSpecialDbBean()
                    cursor.getString(0) //主键值
                    courseCollectSpecialDbBean.column_uid = cursor.getString(1)
                    courseCollectSpecialDbBean.column_course_id = cursor.getString(2)
                    courseCollectSpecialDbBean.column_teacher = cursor.getString(3)
                    courseCollectSpecialDbBean.column_num = cursor.getString(4)
                    courseCollectSpecialDbBean.column_cover = cursor.getString(5)
                    courseCollectSpecialDbBean.column_title = cursor.getString(6)
                    data.add(courseCollectSpecialDbBean)
                }
            } else {
                BKLog.e(TAG, "cursor is null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return data
    }

    /**
     * 是否存在
     */
    fun exist(courseId: String): Boolean {
        return !isSelect("exist", courseId).isEmpty()
    }

    /**
     * 查询数据，如果没有给出相应的提示
     * @param msg 错误信息提示
     * @param sectionId 查询字段
     */
    private fun isSelect(msg: String, courseId: String): ArrayList<CourseCollectSpecialDbBean> {
        val courseCollectSpecialDbBean = selectByCourseId(courseId)
        if (courseCollectSpecialDbBean.isEmpty()) {
            BKLog.e(TAG, "查询失败 - $msg")
            return ArrayList<CourseCollectSpecialDbBean>()
        }
        if (courseCollectSpecialDbBean.size > 1) {
            throw IllegalArgumentException("$msg 数据库中有多条该记录：$courseId")
        }
        return courseCollectSpecialDbBean
    }

    /**
     * 数据是否打开检查
     * @param msg 错误信息提示
     */
    private fun isOpen(msg: String): Boolean {
        if (!db?.isOpen!!) {
            BKLog.e(TAG, "${msg}数据库未打开")
            return false
        }
        return true
    }
}