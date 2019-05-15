package com.ponko.cn.module.m3u8downer.core

import android.database.sqlite.SQLiteDatabase

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
//        private const val COLUMN_NEED_DOWNLOAD_KEY = "need_download_key"
        private const val COLUMN_NOT_DOWNLOAD_TS = "not_download_ts"
//        private const val COLUMN_HAVE_DOWNLOAD_TS = "have_download_ts"
//        private const val COLUMN_HAVA_KEY = "hava_key"
        private const val COLUMN_PROGRESS = "progress"
        private const val COLUMN_TOTAL = "total"

        //建表
        const val SQL_CREATE_TABLE ="CREATE TABLE $TABLE_NAME( " +
                "$COLUMN_KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_M3U8 TEXT NOT NULL," +
                "$COLUMN_NEED_DOWNLOAD_TS TEXT NOT NULL," +
//                "$COLUMN_NEED_DOWNLOAD_KEY TEXT NOT NULL," +
                "$COLUMN_NOT_DOWNLOAD_TS TEXT NOT NULL," +
//                "$COLUMN_HAVE_DOWNLOAD_TS TEXT NOT NULL," +
//                "$COLUMN_HAVA_KEY INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_PROGRESS INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_TOTAL INTEGER NOT NULL DEFAULT 0" +
                ");"

        //增
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_M3U8," +
                "$COLUMN_NEED_DOWNLOAD_TS," +
//                "$COLUMN_NEED_DOWNLOAD_KEY," +
                "$COLUMN_NOT_DOWNLOAD_TS," +
//                "$COLUMN_HAVE_DOWNLOAD_TS," +
//                "$COLUMN_HAVA_KEY," +
                "$COLUMN_PROGRESS," +
                "$COLUMN_TOTAL" +
                ") VALUES (?,?,?,?,?);"

        //删
        const val SQL_DELETE_BY_M3U8 = "DELETE FROM $TABLE_NAME WHERE $COLUMN_M3U8;"
        const val SQL_DELETE_ALL = "DELETE FROM $TABLE_NAME;"

        //改
        const val SQL_UPDATE_BY_M3U8 = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_M3U8=?," +
                "$COLUMN_NEED_DOWNLOAD_TS=?," +
//                "$COLUMN_NEED_DOWNLOAD_KEY=?," +
                "$COLUMN_NOT_DOWNLOAD_TS=?," +
//                "$COLUMN_HAVE_DOWNLOAD_TS=?," +
//                "$COLUMN_HAVA_KEY=?," +
                "$COLUMN_PROGRESS=?," +
                "$COLUMN_TOTAL=?" +
                "WHERE $COLUMN_M3U8=?;"

        //查
        const val SQL_SELECT_BY_M3U8 = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_M3U8=?;"
        const val SQL_SELECT_ALL = "SELECT * FROM $TABLE_NAME;"
    }

    /**
     * 数据库操作类
     */
    class Dao(private var db: SQLiteDatabase?) {

        /**
         * 添加数据
         */
        fun insert(bean: DaoBean) {
            db?.execSQL(M3u8DbContract.Table.SQL_INSERT, arrayOf(
                    bean.m3u8,
                    bean.need_download_ts,
//                    bean.need_download_key,
                    bean.not_download_ts,
//                    bean.have_download_ts,
//                    bean.hava_key,
                    bean.progress,
                    bean.total
            ))
        }

        /**
         * 删除数据
         * @param m3u8 查询需要删除的字段
         */
        fun delete(m3u8: String) {
            db?.execSQL(M3u8DbContract.Table.SQL_DELETE_BY_M3U8, arrayOf(
                    m3u8
            ))
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
        fun update(bean: DaoBean) {
            db?.execSQL(M3u8DbContract.Table.SQL_UPDATE_BY_M3U8, arrayOf(
                    bean.m3u8,
                    bean.need_download_ts,
//                    bean.need_download_key,
                    bean.not_download_ts,
//                    bean.have_download_ts,
//                    bean.hava_key,
                    bean.progress,
                    bean.total,
                    bean.m3u8
            ))
        }

        /**
         * 查询
         */
        fun select(m3u8: String): DaoBean {
            val cursor = db?.rawQuery(M3u8DbContract.Table.SQL_SELECT_BY_M3U8, arrayOf(m3u8))

            val bean = DaoBean()
            if(cursor!=null){
                while (cursor?.moveToNext()) {
                    val id = cursor.getInt(0)
                    bean.m3u8 = cursor.getString(1)
                    bean.need_download_ts = cursor.getString(3)
//                    bean.need_download_key = cursor.getString(4)
                    bean.not_download_ts = cursor.getString(4)
//                    bean.have_download_ts = cursor.getString(6)
//                    bean.hava_key = cursor.getInt(7)
                }
            }

            return bean
        }

        /**
         * 查询所有
         */
        fun selectAll(): ArrayList<DaoBean> {
            val data = ArrayList<DaoBean>()
            val cursor = db?.rawQuery(M3u8DbContract.Table.SQL_SELECT_ALL, null)
            if(cursor!=null){
                while (cursor?.moveToNext()) {
                    val bean = DaoBean()
                    val id = cursor.getInt(0)
                    bean.m3u8 = cursor.getString(1)
                    bean.need_download_ts = cursor.getString(3)
//                    bean.need_download_key = cursor.getString(4)
                    bean.not_download_ts = cursor.getString(4)
//                    bean.have_download_ts = cursor.getString(6)
//                    bean.hava_key = cursor.getInt(7)
                    data.add(bean)
                }
            }
            return data
        }

        /**
         * 关闭数据库
         */
        fun close() {
            if (db?.isOpen == true) {
                db?.close()
            }
        }
    }

    /**
     * 数据库操作实体
     */
    class DaoBean {
        var m3u8: String = ""
        var need_download_ts: String = ""   //需要下载ts url1,url3..
//        @Deprecated("使用not_download_ts字段")
//        var need_download_key: String = ""  //需要下载key
        var not_download_ts = ""            //没有下载的ts url1,url3..
//        @Deprecated("使用not_download_ts字段")
//        var have_download_ts: String = ""   //已经下载的ts json串
//        @Deprecated("使用not_download_ts字段")
//        var hava_key = 0                    //0 代表未下载 1代表已经下载
        var progress = 0                    //文件下载进度(单位B)
        var total = 0                       //文件总大小(单位B)
    }
}