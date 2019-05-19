package com.ponko.cn.module.m3u8downer.core

import com.ponko.cn.bean.CourseDetailCBean
import com.ponko.cn.bean.CoursesDetailCBean
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.FileUtil
import java.io.*


object M3u8Utils {
    /**
     * 解析m3u8文件ts 和 key链接
     */
    fun analysis(inputStream: InputStream?): Pair<String, ArrayList<String>> {
        val ts = ArrayList<String>()
        var key = ""
        val br = BufferedReader(InputStreamReader(inputStream))
        while (true) {
            val line = br.readLine()
            if (line == null) {
                BKLog.d("> m3u8文件解析完成")
                BKLog.d(" key : $key")
                for (t in ts.iterator()) {
                    BKLog.d(" ts  : $t")
                }
                BKLog.d("-----------------------------------")
                return Pair(key, ts)
            }

            //保存key地址
            if (line.startsWith("#EXT-X-KEY")) {
                val startIndex = line.indexOf("\"") + 1
                val endIndex = line.lastIndexOf("\"")
                key = line.substring(startIndex, endIndex)
            }

            //保存ts地址
            if (line.startsWith("http") || line.startsWith("https")) {
                ts.add(line)
            }
        }
    }

    /**
     * m3u8文件写入本地，并且修改文件中key ts 地址，指向本地
     */
    fun writeLocal(inputStream: InputStream?, m3u8: String, path: String, dir: String, m3u8Key: String, m3u8Ts: ArrayList<String>) {
        val outFile = FileUtil.createNewFile(path, dir, "${m3u8FileName(m3u8)}")
        val br = BufferedReader(InputStreamReader(inputStream))
        val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(outFile)))
        var line: String? = null
        var index = 0
        while (true) {
            line = br.readLine()
            if (line == null) break
            if (line.startsWith("#EXT-X-KEY")) {
                val oldKey = m3u8Key
                val newKey = "$path/$dir/${m3u8FileName(oldKey)}"
                line = line.replace(oldKey, newKey)
            }
            if (line.startsWith("http://")) {
                val oldTs = m3u8Ts[index++]
                val newTs = "$path/$dir/${m3u8FileName(oldTs)}"
                line = line.replace(oldTs, newTs)
            }
            bw.write(line + "\r\n")
        }
        bw.close()
        br.close()
    }

    /**
     * 截取url文件名称
     */
    fun m3u8FileName(url: String): String {
        val start = url.lastIndexOf("/") + 1
        val end = url.lastIndexOf("") + 1
        return url.substring(start, end)
    }

    /**
     * 获取m3u8唯一标志
     */
    fun m3u8Unique(m3u8: String): String {
        return m3u8FileName(m3u8).replace(".m3u8", "")
    }

    /**
     * copyInputStream，ps：在解析m3u8時候要使用，因爲inputString一旦被讀了，第二次就不能再读了。
     */
    fun copyInputStream(input: InputStream?): Pair<InputStream, InputStream> {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = input?.read(buffer)!!
        while (len > -1) {
            baos.write(buffer, 0, len)
            len = input.read(buffer)
        }
        baos.flush()
        val stream1 = ByteArrayInputStream(baos.toByteArray())
        val stream2 = ByteArrayInputStream(baos.toByteArray())
        return Pair(stream1, stream2)
    }

    /**
     * str转集合 1,2,3,4, ps：下载m3u8解析时会被使用，下载一个文件就从数据库中移除
     */
    fun strToList(args: String?): ArrayList<String>? {
        val l = args?.split(",")
        val ls = ArrayList<String>()
        for (s in l?.iterator()!!) {
            ls.add(s)
        }
        return ls
    }

    /**
     * 集合转str 1,2,3,4
     */
    fun listToStr(list: List<String>?): String? {
        val sb = StringBuilder()
        for (i in 0..(list?.size!! - 1)) {
            if (i == list.size - 1) {
                sb.append("${list[i]}")
            } else {
                sb.append("${list[i]},")
            }
        }
        return sb.toString()
    }

//    @JvmStatic
//    fun main(args: Array<String>) {
//        val str = "1,2,3,4,5"
//        val list = strToList(str)
//        val str2 = listToStr(list)
//        println(str2)
//    }
}