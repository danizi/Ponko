package com.ponko.cn.db.dao

import android.database.sqlite.SQLiteDatabase
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
        if (isOpen("数据插入失败")) return
        db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_INSERT, arrayOf(
                bean.column_course_id
        ))
    }


    /**
     * 删除
     */
    fun deleteBySectionId(sectionId: String) {
        if (isOpen("删除数据失败")) return
        val courseCollectSectionDbBeans = selectBySectionId(sectionId)
        if (courseCollectSectionDbBeans.isEmpty()) {
            BKLog.e(TAG, "查询失败 - 未删除数据")
            return
        }
        for (bean in courseCollectSectionDbBeans) {
            db?.execSQL(CacheContract.CourseCollectSpecialTable.SQL_DETELE_BY_COURSE_ID, arrayOf(
                    bean.column_course_id
            ))
        }
    }

    /**
     * 更新
     */
    fun updateBySectionId(sectionId: String) {

    }

    /**
     * 查找
     */
    fun selectBySectionId(sectionId: String): ArrayList<CourseCollectSectionDbBean> {
        val data = ArrayList<CourseCollectSectionDbBean>()
        return data
    }

    /**
     * 是否存在
     */
    fun exist(sectionId: String) {

    }

    private fun isOpen(msg: String): Boolean {
        if (!db?.isOpen!!) {
            BKLog.e(TAG, "${msg}数据库未打开")
            return false
        }
        return true
    }
}