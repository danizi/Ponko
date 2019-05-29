package com.ponko.cn.db.dao

import android.database.sqlite.SQLiteDatabase
import android.icu.lang.UCharacter.JoiningGroup.PE
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.bean.CourseCollectSectionDbBean
import com.xm.lib.common.log.BKLog

/**
 * 课程小节Dao
 */
class CourseCollectSectionDao(private var db: SQLiteDatabase?) {
    companion object {
        private const val TAG = "CourseCollectSectionDao"
    }

    /**
     * 插入数据
     */
    fun insert(bean: CourseCollectSectionDbBean) {
        if (!isOpen("数据插入失败")) return
        db?.execSQL(CacheContract.CourseCollectSectionTable.SQL_INSERT, arrayOf(
                bean.column_uid,
                bean.column_course_id,
                bean.column_section_id,
                bean.column_section_name
        ))
    }

    /**
     * 删除
     */
    fun deleteBySectionId(sectionId: String) {
        if (!isOpen("删除数据失败")) return
        if (isSelect("未删除数据", sectionId).isEmpty()) return
        val courseCollectSectionDbBeans = selectBySectionId(sectionId)
        for (bean in courseCollectSectionDbBeans) {
            db?.execSQL(CacheContract.CourseCollectSectionTable.SQL_DELETE_BY_COLUMN_SECTION_ID, arrayOf(
                    bean.column_section_id
            ))
        }
    }

    /**
     * 更新
     */
    fun updateBySectionId(sectionId: String, bean: CourseCollectSectionDbBean) {
        if (!isOpen("更新数据失败")) return
        if (isSelect("未更新数据", sectionId).isEmpty()) return
        db?.execSQL(CacheContract.CourseCollectSectionTable.SQL_UPDATE_BY_COURSE_ID, arrayOf(
                bean.column_uid,
                bean.column_course_id,
                bean.column_section_id,
                bean.column_section_name, sectionId
        ))
    }

    /**
     * 查找-通过课程小节来查询
     */
    fun selectBySectionId(sectionId: String): ArrayList<CourseCollectSectionDbBean> {
        val data = ArrayList<CourseCollectSectionDbBean>()
        val cursor = db?.rawQuery(CacheContract.CourseCollectSectionTable.SQL_SELECT_BY_SECTION_ID, arrayOf(sectionId))
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val courseCollectSectionDbBean = CourseCollectSectionDbBean()
                cursor.getColumnName(0) //主键值
                courseCollectSectionDbBean.column_uid = cursor.getString(1)
                courseCollectSectionDbBean.column_course_id = cursor.getString(2)
                courseCollectSectionDbBean.column_section_id = cursor.getString(3)
                courseCollectSectionDbBean.column_section_name = cursor.getString(4)
                data.add(courseCollectSectionDbBean)
            }
        } else {
            BKLog.e(TAG, "cursor is null")
        }
        return data
    }

    /**
     * 查找-通过专题id来查询
     */
    fun selectByCourseId(courseId: String): ArrayList<CourseCollectSectionDbBean> {
        val data = ArrayList<CourseCollectSectionDbBean>()
        val cursor = db?.rawQuery(CacheContract.CourseCollectSectionTable.SQL_SELECT_BY_COURSE_ID, arrayOf(courseId))
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val courseCollectSectionDbBean = CourseCollectSectionDbBean()
                cursor.getColumnName(0) //主键值
                courseCollectSectionDbBean.column_uid = cursor.getString(1)
                courseCollectSectionDbBean.column_course_id = cursor.getString(2)
                courseCollectSectionDbBean.column_section_id = cursor.getString(3)
                courseCollectSectionDbBean.column_section_name = cursor.getString(4)
                data.add(courseCollectSectionDbBean)
            }
        } else {
            BKLog.e(TAG, "cursor is null")
        }
        return data
    }

    /**
     * 查找所有
     */
    fun selectAll(): ArrayList<CourseCollectSectionDbBean> {
        val data = ArrayList<CourseCollectSectionDbBean>()
        val cursor = db?.rawQuery(CacheContract.CourseCollectSectionTable.SQL_SELECT_ALL, arrayOf())
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val courseCollectSectionDbBean = CourseCollectSectionDbBean()
                cursor.getColumnName(0) //主键值
                courseCollectSectionDbBean.column_uid = cursor.getString(1)
                courseCollectSectionDbBean.column_course_id = cursor.getString(2)
                courseCollectSectionDbBean.column_section_id = cursor.getString(3)
                courseCollectSectionDbBean.column_section_name = cursor.getString(4)
                data.add(courseCollectSectionDbBean)
            }
        } else {
            BKLog.e(TAG, "cursor is null")
        }
        return data
    }

    /**
     * 是否存在
     */
    fun exist(sectionId: String): Boolean {
        return !isSelect("exist", sectionId).isEmpty()
    }

    /**
     * 查询数据，如果没有给出相应的提示
     * @param msg 错误信息提示
     * @param sectionId 查询字段
     */
    private fun isSelect(msg: String, sectionId: String): ArrayList<CourseCollectSectionDbBean> {
        val courseCollectSectionDbBeans = selectBySectionId(sectionId)
        if (courseCollectSectionDbBeans.isEmpty()) {
            BKLog.e(TAG, "查询失败 - $msg")
            return ArrayList<CourseCollectSectionDbBean>()
        }
        if (courseCollectSectionDbBeans.size > 1) {
            throw IllegalArgumentException("$msg 数据库中有多条该记录：$sectionId")
        }
        return courseCollectSectionDbBeans
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