package com.ponko.cn.db.dao

import android.database.sqlite.SQLiteDatabase
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.CacheContract.CourseTable.SQL_SELECT_BY_SPECIAL_ID
import com.ponko.cn.db.CacheContract.CourseTable.SQL_SELECT_BY_VID
import com.ponko.cn.db.bean.CourseDbBean
import com.xm.lib.common.log.BKLog

/**
 * 专题-课程操作类数据库类
 */
class CourseDao(private var db: SQLiteDatabase?) {

    /**
     * 插入数据
     */
    fun insert(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            if (select(bean).isEmpty() && !exist(bean.column_vid)) {
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
                        bean.column_down_path,
                        bean.column_state,
                        bean.column_vid
                ))
                //db?.close()
            } else {
                BKLog.d("课程已存在")
            }
        } else {
            BKLog.d("数据库未打开")
        }
    }

    /**
     * 课程下载完成调用该方法进行状态更新
     * 更新状态
     */
    fun downCompleteUpdate(vid: String, cacheM3u8: String, m3u8: String, complete: Int) {
        if (!select(vid).isEmpty()) {
            val bean = select(vid)[0]
            bean.column_state = CourseDbBean.DOWN_STATE_COMPLETE
            bean.column_down_path = cacheM3u8
            bean.column_m3u8_url = m3u8
            bean.column_complete = 1
            update(bean)
        } else {
            BKLog.e("下载完成,数据库更新失败")
        }
    }

    /**
     * 课程下载过程中调用该方法进行状态更新
     */
    fun downProgressUpdate(vid: String, progress: Int) {
        if (!select(vid).isEmpty()) {
            val bean = select(vid)[0]
            bean.column_state = CourseDbBean.DOWN_STATE_PROCESS
            bean.column_progress = progress
            bean.column_complete = 0
            update(bean)
        } else {
            BKLog.e("下载更新,数据库更新失败")
        }
    }

    /**
     * 课程加入队列中调用该方法进行状态更新
     */
    fun downQueueUpdate(vid: String) {
        if (!select(vid).isEmpty()) {
            val bean = select(vid)[0]
            bean.column_state = CourseDbBean.DOWN_STATE_READY
            bean.column_complete = 0
            update(bean)
        } else {
            BKLog.e("加入队列,数据库更新失败")
        }
    }

    /**
     * 课程下载错误回调用该方法进行状态更新
     */
    fun downErrorUpdate(vid: String) {
        if (!select(vid).isEmpty()) {
            val bean = select(vid)[0]
            bean.column_state = CourseDbBean.DOWN_STATE_ERROR
            bean.column_complete = 0
            update(bean)
        } else {
            BKLog.e("下载错误,数据库更新失败")
        }
    }

    fun downStateUpdate(vid: String) {
        if (!select(vid).isEmpty()) {
            val bean = select(vid)[0]
            bean.column_state = CourseDbBean.DOWN_STATE_START
            bean.column_complete = 0
            update(bean)
        } else {
            BKLog.e("下载状态,数据库更新失败")
        }
    }

    /**
     * 请求视频接口信息调用此方法
     */
    fun requestVideoInfoUpdate(vid: String?, m3u8: String, total: Int) {
        if (select(vid).size > 0) {
            val bean = select(vid)[0]
            bean.column_m3u8_url = m3u8
            bean.column_total = total
            if (bean != null) {
                db?.execSQL(CacheContract.CourseTable.SQL_UPDATE_BY_VID, arrayOf(
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
                        bean.column_down_path,
                        bean.column_state,
                        bean.column_vid, vid))
            }
        }
    }

    /**
     * 通过课程id来删除课程
     */
    fun delete(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_DEL_BY_ID, arrayOf(bean.column_course_id))
            //db?.close()
        }
    }

    /**
     * 通过vid来删除课程
     */
    fun deleteByVid(bean: CourseDbBean){
        db?.execSQL(CacheContract.CourseTable.SQL_DEL_BY_VID, arrayOf(bean.column_vid))
    }

    /**
     * 删除所有课程信息
     */
    fun deleteAll() {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_DEL_ALL, null)
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }

    /**
     * 通过课程vid查询
     */
    fun select(bean: CourseDbBean): ArrayList<CourseDbBean> {
        val queryData = ArrayList<CourseDbBean>()
        if (db?.isOpen == true) {
            val cursor = db?.rawQuery(CacheContract.CourseTable.SQL_SELECT_BY_ID, arrayOf(bean.column_course_id))
            if (cursor == null) {
                BKLog.e("从数据库中未查找到内容")
                return queryData
            }
            while (cursor.moveToNext()) {
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
                courseDbBean.column_state = cursor.getString(12)
                courseDbBean.column_vid = cursor.getString(13)
                queryData.add(courseDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        if (queryData.size >= 2) {
            throw IllegalArgumentException("通过课程id查询到多个课程，应该有且只有一个。")
        }
        return queryData
    }

    /**
     * 通过课程vid查询
     */
    fun select(vid: String?): ArrayList<CourseDbBean> {
        val data = ArrayList<CourseDbBean>()
        val cursor = db?.rawQuery(SQL_SELECT_BY_VID, arrayOf(vid))
        if (cursor != null) {
            while (cursor.moveToNext()) {
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
                courseDbBean.column_state = cursor.getString(12)
                courseDbBean.column_vid = cursor.getString(13)
                data.add(courseDbBean)
            }
        } else {
            BKLog.e("数据库中未查询到")
        }
        if (data.size >= 2) {
            throw IllegalArgumentException("通过课程vid查询到多个课程，应该有且只有一个。")
        }
        return data
    }

    /**
     * 通过专题ID来查看课程
     */
    fun selectBySpecialId(special_id: String): ArrayList<CourseDbBean> {
        val data = ArrayList<CourseDbBean>()
        val cursor = db?.rawQuery(SQL_SELECT_BY_SPECIAL_ID, arrayOf(special_id))
        if (cursor != null) {
            while (cursor.moveToNext()) {
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
                courseDbBean.column_state = cursor.getString(12)
                courseDbBean.column_vid = cursor.getString(13)
                data.add(courseDbBean)
            }
        } else {
            BKLog.e("数据库中未查询到")
        }
        return data
    }

    /**
     * 查看所有课程
     */
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
                courseDbBean.column_state = cursor.getString(12)
                courseDbBean.column_vid = cursor.getString(13)
                queryAllData.add(courseDbBean)
            }
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
        return queryAllData
    }

    /**
     * 判断是否存在
     */
    fun exist(vid: String?): Boolean {
        return select(vid).size > 0
    }

    /**
     * 判断是否已经缓存完成
     */
    fun isComplete(vid: String?): Boolean {
        val courseDbBeans = select(vid)
        if (courseDbBeans.isEmpty()) {
            return false
        }
        if (courseDbBeans.size > 1) {
            throw IllegalAccessException("查询到多个相同的vid信息，按道理只有一个")
        }
        return courseDbBeans[0].column_complete == 1
    }

    /**
     * 判断完成的数量
     */
    fun completeCount(specialId: String): Int {
        var count = 0
        val courseDbBeans = selectBySpecialId(specialId)
        for (courseDbBean in courseDbBeans) {
            if (courseDbBean.column_complete == 1) {
                count++
            }
        }
        return count
    }


    private fun update(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_UPDATE_BY_ID, arrayOf(
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
                    bean.column_down_path,
                    bean.column_state,
                    bean.column_vid, bean.column_course_id))
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }

    @Deprecated("")
    private fun update(vid: String, cacheM3u8: String) {
        val bean = select(vid)[0]
        bean.column_m3u8_url = cacheM3u8
        if (bean != null) {
            db?.execSQL(CacheContract.CourseTable.SQL_UPDATE_BY_VID, arrayOf(
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
                    bean.column_down_path,
                    bean.column_state,
                    bean.column_vid, vid))
        }
    }

    @Deprecated("")
    private fun updateProgressByUrl(bean: CourseDbBean) {
        if (db?.isOpen == true) {
            db?.execSQL(CacheContract.CourseTable.SQL_UPDATE_BY_ID, arrayOf(
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
                    bean.column_down_path,
                    bean.column_state,
                    bean.column_vid, bean.column_course_id))
            //db?.close()
        } else {
            BKLog.d("数据库未打开")
        }
    }
}