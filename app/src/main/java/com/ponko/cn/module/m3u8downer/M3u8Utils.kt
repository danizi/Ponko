package com.ponko.cn.module.m3u8downer

import android.os.Environment
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
                BKLog.d("文件读取完成")
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
        BKLog.d("********************")
        BKLog.d("mu38文件截取key:$key")
        for (t in ts?.iterator()) {
            BKLog.d("mu38文件截取ts:$t")
        }
        BKLog.d("********************")
        return Pair(key, ts)
    }

    /**
     * m3u8文件写入本地，并且修改文件中key ts 地址，指向本地
     */
    fun writeLocal(inputStream: InputStream?, m3u8: String, dir: String, m3u8Key: String, m3u8Ts: ArrayList<String>) {
        val outFile = FileUtil.createNewFile(Environment.getExternalStorageDirectory().canonicalPath, "$dir/${M3u8Utils.m3u8FileName(m3u8)}", "${M3u8Utils.m3u8FileName(m3u8)}.m3u8")
        val br = BufferedReader(InputStreamReader(inputStream))
        val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(outFile)))
        var line: String? = null
        var index = 0
        while (true) {
            line = br.readLine()
            if (line == null) break
            if(line.startsWith("#EXT-X-KEY")){
                val oldKey = m3u8Key
                val newKey =Environment.getExternalStorageDirectory().canonicalPath+"/$dir/${M3u8Utils.m3u8FileName(m3u8)}/${M3u8Utils.m3u8FileName(oldKey)}.key"
                line = line.replace(oldKey,newKey)
            }
            if (line.startsWith("http://")) {
                val oldTs =  m3u8Ts[index++]
                val newTs = Environment.getExternalStorageDirectory().canonicalPath+"/$dir/${M3u8Utils.m3u8FileName(m3u8)}/${M3u8Utils.m3u8FileName(oldTs)}.ts"
                line = line.replace(oldTs,newTs)
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
        val end = url.lastIndexOf(".")
        val name = url.substring(start, end)
        BKLog.d("${url}解析m3u8文件名称：$name")
        return name
    }

    /**
     * copyInputStream
     */
    fun copyInputStream(input: InputStream?):Pair<InputStream,InputStream> {
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
        return Pair(stream1,stream2)
    }
}