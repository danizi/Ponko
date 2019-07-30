package com.ponko.cn.module.m3u8downer.core

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.ponko.cn.db.CacheContract
import com.ponko.cn.db.bean.CourseDbBean
import com.xm.lib.common.log.BKLog
import java.nio.file.Files.exists

/**
 * 数据库契约类
 */
class M3u8DbContract {
    /**
     * SQL语句
     */
    object Table {
        private const val TABLE_NAME = "M3u8DownTable"
        private const val COLUMN_KEY_ID = "id"
        private const val COLUMN_M3U8 = "m3u8"
        private const val COLUMN_NEED_DOWNLOAD_TS = "need_download_ts"
        private const val COLUMN_NOT_DOWNLOAD_TS = "not_download_ts"
        private const val COLUMN_PROGRESS = "progress"
        private const val COLUMN_TOTAL = "total"
        private const val COLUMN_COMPLETE = "complete"
        private const val COLUMN_VID = "vid"

        //建表
        const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME( " +
                "$COLUMN_KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_M3U8 TEXT NOT NULL," +
                "$COLUMN_NEED_DOWNLOAD_TS TEXT NOT NULL," +
                "$COLUMN_NOT_DOWNLOAD_TS TEXT NOT NULL," +
                "$COLUMN_PROGRESS INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_TOTAL INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_COMPLETE INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_VID TEXT NOT NULL" +
                ");"

        //增
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_M3U8," +
                "$COLUMN_NEED_DOWNLOAD_TS," +
                "$COLUMN_NOT_DOWNLOAD_TS," +
                "$COLUMN_PROGRESS," +
                "$COLUMN_TOTAL," +
                "$COLUMN_COMPLETE," +
                "$COLUMN_VID) VALUES (?,?,?,?,?,?,?);"

        //删
        @Deprecated("")
        const val SQL_DELETE_BY_M3U8 = "DELETE FROM $TABLE_NAME WHERE $COLUMN_M3U8 = ?;"
        const val SQL_DELETE_BY_VID = "DELETE FROM $TABLE_NAME WHERE $COLUMN_VID = ?;"
        const val SQL_DELETE_ALL = "DELETE FROM $TABLE_NAME;"

        //改
        @Deprecated("")
        const val SQL_UPDATE_BY_M3U8 = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_M3U8=?," +
                "$COLUMN_NEED_DOWNLOAD_TS=?," +
                "$COLUMN_NOT_DOWNLOAD_TS=?," +
                "$COLUMN_PROGRESS=?," +
                "$COLUMN_TOTAL=?," +
                "$COLUMN_COMPLETE=?" +
                "WHERE $COLUMN_M3U8=?;"

        const val SQL_UPDATE_BY_VID = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_M3U8=?," +
                "$COLUMN_NEED_DOWNLOAD_TS=?," +
                "$COLUMN_NOT_DOWNLOAD_TS=?," +
                "$COLUMN_PROGRESS=?," +
                "$COLUMN_TOTAL=?," +
                "$COLUMN_COMPLETE=?," +
                "$COLUMN_VID=?" +
                "WHERE $COLUMN_VID=?;"
        //查
        @Deprecated("")
        const val SQL_SELECT_BY_M3U8 = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_M3U8=?;"
        const val SQL_SELECT_BY_VID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_VID=?;"
        const val SQL_SELECT_ALL = "SELECT * FROM $TABLE_NAME;"
    }

    /**
     * 数据库操作类
     */
    class Dao(private var db: SQLiteDatabase?) {
        companion object {
            private const val TAG = "M3u8Dao"
        }

        /**
         * 添加数据
         */
        @Deprecated("")
        fun insert(bean: DaoBean) {
            if (!exists(bean.m3u8)) {
                db?.execSQL(M3u8DbContract.Table.SQL_INSERT, arrayOf(
                        bean.m3u8,
                        bean.need_download_ts,
                        bean.not_download_ts,
                        bean.progress,
                        bean.total,
                        bean.complete,
                        bean.vid
                ))
            } else {
                BKLog.e(TAG, "插入${bean.toString()}数据失败，数据库不存在该条记录")
            }
        }

        fun insert2(bean: DaoBean) {
            if (!exists2(bean.vid)) {
                db?.execSQL(M3u8DbContract.Table.SQL_INSERT, arrayOf(
                        bean.m3u8,
                        bean.need_download_ts,
                        bean.not_download_ts,
                        bean.progress,
                        bean.total,
                        bean.complete,
                        bean.vid
                ))
            } else {
                BKLog.e(TAG, "插入${bean.toString()}数据失败，数据库不存在该条记录")
            }
        }

        /**
         * 删除数据
         * @param m3u8 查询需要删除的字段
         */
        fun delete(m3u8: String) {
            if (exists(m3u8)) {
                db?.execSQL(M3u8DbContract.Table.SQL_DELETE_BY_M3U8, arrayOf(m3u8))
            } else {
                BKLog.e(TAG, "删除${m3u8}对应数据失败，数据库不存在该条记录")
            }
        }

        fun delete2(vid: String) {
            if (exists2(vid)) {
                db?.execSQL(M3u8DbContract.Table.SQL_DELETE_BY_VID, arrayOf(vid))
            } else {
                BKLog.e(TAG, "删除${vid}对应数据失败，数据库不存在该条记录")
            }
        }

        /**
         * 删除所有
         */
        fun deleteAll() {
            db?.execSQL(M3u8DbContract.Table.SQL_DELETE_ALL, null)
        }

        /**
         * 更新
         * @param bean 先通过m3u8获取DaoBean，然后修改需要修改字段
         */
        @Deprecated("")
        fun update(bean: DaoBean) {
            if (exists(bean.m3u8)) {
                db?.execSQL(M3u8DbContract.Table.SQL_UPDATE_BY_M3U8, arrayOf(
                        bean.m3u8,
                        bean.need_download_ts,
                        bean.not_download_ts,
                        bean.progress,
                        bean.total,
                        bean.complete,
                        bean.m3u8
                ))
            } else {
                BKLog.e(TAG, "更新${bean.toString()}对应数据失败，数据库不存在该条记录")
            }
        }

        fun update2(bean: DaoBean) {
            if (exists2(bean.vid)) {
                db?.execSQL(M3u8DbContract.Table.SQL_UPDATE_BY_VID, arrayOf(
                        bean.m3u8,
                        bean.need_download_ts,
                        bean.not_download_ts,
                        bean.progress,
                        bean.total,
                        bean.complete,
                        bean.vid,
                        bean.vid
                ))
            } else {
                BKLog.e(TAG, "更新${bean.toString()}对应数据失败，数据库不存在该条记录")
            }
        }

        /**
         * 查询
         */
        @Deprecated("")
        fun select(m3u8: String): DaoBean {
            val bean = DaoBean()
            var cursor: Cursor? = null
            try {
                cursor = db?.rawQuery(M3u8DbContract.Table.SQL_SELECT_BY_M3U8, arrayOf(m3u8))
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(0)
                        bean.m3u8 = cursor.getString(1)
                        bean.need_download_ts = cursor.getString(2)
                        bean.not_download_ts = cursor.getString(3)
                        bean.progress = cursor.getInt(4)
                        bean.total = cursor.getInt(5)
                        bean.complete = cursor.getInt(6)
                    }
                } else {
                    BKLog.e("通过m3u8在数据库中未查询到")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
            return bean
        }

        fun select2(vid: String): DaoBean {
            val bean = DaoBean()
            var cursor: Cursor? = null
            try {
                cursor = db?.rawQuery(M3u8DbContract.Table.SQL_SELECT_BY_VID, arrayOf(vid))
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val id = cursor.getInt(0)
                        bean.m3u8 = cursor.getString(1)
                        bean.need_download_ts = cursor.getString(2)
                        bean.not_download_ts = cursor.getString(3)
                        bean.progress = cursor.getInt(4)
                        bean.total = cursor.getInt(5)
                        bean.complete = cursor.getInt(6)
                        bean.vid = cursor.getString(7)
                    }
                } else {
                    BKLog.e("通过vid在数据库中未查询到")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }

            return bean
        }

        /**
         * 查询所有
         */
        fun selectAll(): ArrayList<DaoBean> {
            val data = ArrayList<DaoBean>()
            var cursor:Cursor? = null
            try {
                 cursor = db?.rawQuery(M3u8DbContract.Table.SQL_SELECT_ALL, null)
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val bean = DaoBean()
                        val id = cursor.getInt(0)
                        bean.m3u8 = cursor.getString(1)
                        bean.need_download_ts = cursor.getString(2)
                        bean.not_download_ts = cursor.getString(3)
                        bean.progress = cursor.getInt(4)
                        bean.total = cursor.getInt(5)
                        bean.complete = cursor.getInt(6)
                        bean.vid = cursor.getString(7)
                        data.add(bean)
                    }
                }else{
                    BKLog.e("数据库中未查询到所有任务")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }

            return data
        }

        /**
         * 关闭数据库
         */
        fun close() {
            if (db?.isOpen == true) {
                db?.close()
            } else {
                BKLog.d(TAG, "关闭数据库失败，该数据库未打开")
            }
        }

        /**
         * 是否存在
         */
        @Deprecated("")
        fun exists(m3u8: String): Boolean {
            val dbBean = select(m3u8)
            return !TextUtils.isEmpty(dbBean.m3u8)
        }

        /**
         * 是否存在
         */
        fun exists2(vid: String): Boolean {
            val dbBean = select2(vid)
            return !TextUtils.isEmpty(dbBean.vid)
        }
    }

    /**
     * 数据库操作实体
     */
    class DaoBean {
        @Deprecated("")
        var m3u8: String = ""
        var need_download_ts: String = ""   //需要下载ts url1,url3..
        var not_download_ts = ""            //没有下载的ts url1,url3..
        var progress = 0                    //文件下载进度(单位B)
        var total = 0                       //文件总大小(单位B)
        var complete = 0                    //1 代表已经下载完成 0代表未下载完成
        var vid = ""                        //视频唯一标识

        override fun toString(): String {
            return "DaoBean(m3u8='$m3u8', need_download_ts='$need_download_ts', not_download_ts='$not_download_ts', progress=$progress, total=$total, complete=$complete, vid=$vid)"
        }
    }
}