package com.ponko.cn.db

/**
 * 课程划分层次：系列、 专题 、课程列表。 PS:查看学习页面一层一层点击感受一下
 */
class CacheContract {
    /**
     * 缓存专题
     */
    object CourseSpecialTable {
        private const val TABLE_NAME = "CourseSpecialTable"      //表名称
        private const val PRIMARY_ID = "_id"                            //主键ID
        private const val COLUMN_UID = "uid"                     //用户唯一标识 PS：关联用户表
        private const val COLUMN_SPECIAL_ID = "special_id"       //学习专题id
        private const val COLUMN_TEACHER = "teacher"             //专题老师
        private const val COLUMN_NUM = "num"                     //专题课程数量
        private const val COLUMN_COVER = "cover"                 //专题封面
        private const val COLUMN_TITLE = "title"                 //专题标题

        //创建表语法
        const val SQL_CREATE_COURSE_SPECIAL_TABLE = "CREATE TABLE $TABLE_NAME(" +
                "$PRIMARY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_UID TEXT NOT NULL," +
                "$COLUMN_SPECIAL_ID TEXT NOT NULL," +
                "$COLUMN_TEACHER TEXT NOT NULL," +
                "$COLUMN_NUM INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_COVER TEXT NOT NULL," +
                "$COLUMN_TITLE TEXT NOT NULL" +
                ");"

        //增 插入数据
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_UID," +
                "$COLUMN_SPECIAL_ID," +
                "$COLUMN_TEACHER," +
                "$COLUMN_NUM," +
                "$COLUMN_COVER," +
                "$COLUMN_TITLE" +
                ") VALUES (?,?,?,?,?,?);"

        //删 删除数据
        const val SQL_DEL_BY_ID = "DELETE FROM $TABLE_NAME WHERE $COLUMN_SPECIAL_ID = ?;"
        const val SQL_DEL_All = "DELETE FROM $TABLE_NAME;"

        //改 修改数据
        const val SQL_UPDATE_BY_ID = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_UID=?," +
                "$COLUMN_SPECIAL_ID=?," +
                "$COLUMN_TEACHER=?," +
                "$COLUMN_NUM=?," +
                "$COLUMN_COVER=?," +
                "$COLUMN_TITLE=?" +
                " WHERE $COLUMN_SPECIAL_ID=?;"

        //查 查询数据
        const val SQL_SELECT_ALL = "SELECT * FROM $TABLE_NAME;"
        const val SQL_SELECT_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SPECIAL_ID = ?;"
    }

    /**
     * 缓存课程表
     */
    object CourseTable {
        private const val TABLE_NAME = "CourseTable"           //表名称
        private const val PRIMARY_ID = "_id"                   //主键ID
        private const val COLUMN_UID = "uid"                   //用户唯一标识
        private const val COLUMN_SPECIAL_ID = "special_id"     //专题id         PS : 关联缓存专题表，是一对多的关系
        private const val COLUMN_COURSE_ID = "course_id"       //课程id
        private const val COLUMN_COVER = "cover"               //封面
        private const val COLUMN_TITLE = "title"               //课程标题
        private const val COLUMN_TOTAL = "total"               //单个课程大小 单位（B）
        private const val COLUMN_PROGRESS = "progress"         //单个课程下载进度 单位（B）
        private const val COLUMN_COMPLETE = "complete"         //是否下载完成
        private const val COLUMN_M3U8_URL = "m3u8_url"         //下载的m3u8地址
        @Deprecated("")
        private const val COLUMN_KEY_TS_URL = "key_ts_url"     //key ts 地址
        private const val COLUMN_DOWN_PATH = "down_path"       //下载路径
        private const val COLUMN_COLUMN_STATE = "column_state" //下载路径
        private const val COLUMN_COLUMN_VID = "column_vid"     //下载路径

        //创建
        const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME( " +
                "$PRIMARY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_UID TEXT NOT NULL," +
                "$COLUMN_SPECIAL_ID TEXT NOT NULL," +
                "$COLUMN_COURSE_ID TEXT NOT NULL," +
                "$COLUMN_COVER TEXT NOT NULL," +
                "$COLUMN_TITLE TEXT NOT NULL," +
                "$COLUMN_TOTAL INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_PROGRESS INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_COMPLETE INTEGER NOT NULL DEFAULT 0," +
                "$COLUMN_M3U8_URL TEXT NOT NULL," +
                "$COLUMN_KEY_TS_URL TEXT NOT NULL," +
                "$COLUMN_DOWN_PATH TEXT NOT NULL," +
                "$COLUMN_COLUMN_STATE TEXT NOT NULL," +
                "$COLUMN_COLUMN_VID TEXT NOT NULL" +
                ");"
        //增
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_UID," +
                "$COLUMN_SPECIAL_ID," +
                "$COLUMN_COURSE_ID," +
                "$COLUMN_COVER," +
                "$COLUMN_TITLE," +
                "$COLUMN_TOTAL," +
                "$COLUMN_PROGRESS," +
                "$COLUMN_COMPLETE," +
                "$COLUMN_M3U8_URL," +
                "$COLUMN_KEY_TS_URL," +
                "$COLUMN_DOWN_PATH," +
                "$COLUMN_COLUMN_STATE," +
                "$COLUMN_COLUMN_VID" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"

        //删
        const val SQL_DEL_BY_ID = "DELETE FROM $TABLE_NAME WHERE $COLUMN_COURSE_ID = ?;"
        const val SQL_DEL_BY_VID = "DELETE FROM $TABLE_NAME WHERE $COLUMN_COLUMN_VID = ?;"
        const val SQL_DEL_ALL = "DELETE FROM $TABLE_NAME"

        //改
        const val SQL_UPDATE_BY_ID = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_UID=?," +
                "$COLUMN_SPECIAL_ID=?," +
                "$COLUMN_COURSE_ID=?," +
                "$COLUMN_COVER=?," +
                "$COLUMN_TITLE=?," +
                "$COLUMN_TOTAL=?," +
                "$COLUMN_PROGRESS=?," +
                "$COLUMN_COMPLETE=?," +
                "$COLUMN_M3U8_URL=?," +
                "$COLUMN_KEY_TS_URL=?," +
                "$COLUMN_DOWN_PATH=?," +
                "$COLUMN_COLUMN_STATE=?," +
                "$COLUMN_COLUMN_VID=? WHERE $COLUMN_COURSE_ID=?;"

        const val SQL_UPDATE_BY_VID = "UPDATE $TABLE_NAME SET " +
                "$COLUMN_UID=?," +
                "$COLUMN_SPECIAL_ID=?," +
                "$COLUMN_COURSE_ID=?," +
                "$COLUMN_COVER=?," +
                "$COLUMN_TITLE=?," +
                "$COLUMN_TOTAL=?," +
                "$COLUMN_PROGRESS=?," +
                "$COLUMN_COMPLETE=?," +
                "$COLUMN_M3U8_URL=?," +
                "$COLUMN_KEY_TS_URL=?," +
                "$COLUMN_DOWN_PATH=?," +
                "$COLUMN_COLUMN_STATE=?," +
                "$COLUMN_COLUMN_VID=? WHERE $COLUMN_COLUMN_VID=?;"
        //查
        const val SQL_SELECT_BY_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_COURSE_ID = ?;"
        const val SQL_SELECT_BY_VID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_COLUMN_VID = ?;"
        const val SQL_SELECT_BY_SPECIAL_ID = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SPECIAL_ID = ?;"
        const val SQL_SELECT_ALL = "SELECT * FROM $TABLE_NAME;"
    }

    /**
     * 收藏 - 专题
     */
    object CourseCollectSpecialTable {
        //表名
        private const val TABLE_NAME = "CourseCollectSpecialTable"
        //表-列
        private const val PRIMARY_ID = "_id"                     //主键ID
        private const val COLUMN_UID = "uid"                     //用户唯一标识 PS：关联用户表
        private const val COLUMN_COURSE_ID = "course_id"         //学习专题id 即是课程id
        private const val COLUMN_TEACHER = "teacher"             //专题老师
        private const val COLUMN_NUM = "num"                     //专题课程数量
        private const val COLUMN_COVER = "cover"                 //专题封面
        private const val COLUMN_TITLE = "title"                 //专题标题
        //创建表
        const val SQL_CREATE_COURSE_COLLECT_SPECIAL_TABLE = "CREATE TABLE $TABLE_NAME(" +
                "$PRIMARY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_UID TEXT NOT NULL," +
                "$COLUMN_COURSE_ID TEXT NOT NULL," +
                "$COLUMN_TEACHER TEXT NOT NULL," +
                "$COLUMN_NUM TEXT NOT NULL," +
                "$COLUMN_COVER TEXT NOT NULL," +
                "$COLUMN_TITLE TEXT NOT NULL" +
                ");"

        //增
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_UID," +
                "$COLUMN_COURSE_ID," +
                "$COLUMN_TEACHER," +
                "$COLUMN_NUM," +
                "$COLUMN_COVER," +
                "$COLUMN_TITLE" +
                ") VALUES (?,?,?,?,?,?)"

        //删
        const val SQL_DETELE_BY_COURSE_ID="DELETE FROM $TABLE_NAME WHERE $COLUMN_COURSE_ID = ?;"
        const val SQL_DETELE_BY_ALL="DELETE FROM $TABLE_NAME;"

        //改
        const val SQL_UPDATE_BY_COURSE_ID="UPDATE $TABLE_NAME SET " +
                "$COLUMN_UID =?," +
                "$COLUMN_COURSE_ID =?," +
                "$COLUMN_TEACHER =?," +
                "$COLUMN_NUM =?," +
                "$COLUMN_COVER =?," +
                "$COLUMN_TITLE =?" +
                "WHERE $COLUMN_COURSE_ID=?;"
        //查
        const val SQL_SELECT_BY_COURSE_ID="SELECT * FROM $TABLE_NAME WHERE $COLUMN_COURSE_ID = ?;"
        const val SQL_SELECT_BY_ALL="SELECT * FROM $TABLE_NAME;"
    }

    /**
     * 收藏 - 小结收藏
     */
    object CourseCollectSectionTable {
        //表名
        private const val TABLE_NAME = "CourseCollectSectionTable"
        //表-列
        private const val PRIMARY_ID = "_id"                           //主键ID
        private const val COLUMN_UID = "uid"                           //用户唯一标识 PS：关联用户表
        private const val COLUMN_COURSE_ID = "course_id"               //学习专题id 即是课程id
        private const val COLUMN_SECTION_ID = "section_id"             //小节id
        private const val COLUMN_SECTION_NAME = "column_section_name"  //小节名称
        //创建表
        const val SQL_CREATE_COURSE_COLLECT_SECTION_TABLE = "CREATE TABLE $TABLE_NAME(" +
                "$PRIMARY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_UID TEXT NOT NULL," +
                "$COLUMN_COURSE_ID TEXT NOT NULL," +
                "$COLUMN_SECTION_ID TEXT NOT NULL," +
                "$COLUMN_SECTION_NAME TEXT NOT NULL" +
                ")"
        //增
        const val SQL_INSERT = "INSERT INTO $TABLE_NAME(" +
                "$COLUMN_UID," +
                "$COLUMN_COURSE_ID," +
                "$COLUMN_SECTION_ID," +
                "$COLUMN_SECTION_NAME" +
                ") VALUES (?,?,?,?)"
        //删
        const val SQL_DELETE_BY_COLUMN_SECTION_ID= "DELETE FROM $TABLE_NAME WHERE $COLUMN_SECTION_ID = ?;"
        const val SQL_DELETE_ALL = "DELETE FROM $TABLE_NAME;"

        //改
        const val SQL_UPDATE_BY_COURSE_ID="UPDATE $TABLE_NAME SET" +
                "$COLUMN_UID=?," +
                "$COLUMN_COURSE_ID=?," +
                "$COLUMN_SECTION_ID=?," +
                "$COLUMN_SECTION_NAME=?" +
                "WHERE $COLUMN_COURSE_ID=?;"

        //查
        const val SQL_SELECT_BY_SECTION_ID="SELECT * FROM $TABLE_NAME WHERE $COLUMN_SECTION_ID = ?;"
        const val SQL_SELECT_BY_COURSE_ID="SELECT * FROM $TABLE_NAME WHERE $COLUMN_COURSE_ID = ?;"
        const val SQL_SELECT_ALL="SELECT * FROM $TABLE_NAME"
    }


}