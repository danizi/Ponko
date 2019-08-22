package com.ponko.cn.module.m3u8downer.core

import com.xm.lib.common.log.BKLog
import java.util.concurrent.*

/**
 * 下载者分发器
 * 设置一个运行队列阈值 N
 * 1 任务队列 < N ，任务添加到“运行”队列，并加入到线程池中运行。
 * 2 任务队列 > N ，任务添加到“准备”队列，等待执行 PS ：任务线程完成当调用remove(...)方法时，会执行准备队列中的任务。
 */
class M3u8Dispatcher private constructor(builder: Builder) : IM3u8Dispatcher {
    companion object {
        private const val TAG = "M3u8Dispatcher"
    }

    var pool: ExecutorService? = null
    var runqueues = 1
    var runningQueue: BlockingQueue<M3u8DownTasker>? = null
    var readyQueue: BlockingQueue<M3u8DownTasker>? = null

    constructor() : this(Builder())

    init {
        this.pool = builder.pool
        this.runqueues = builder.runqueues
        this.runningQueue = builder.runningQueue
        this.readyQueue = builder.readyQueue
    }

    @Synchronized
    override fun enqueue(tasker: M3u8DownTasker?) {
        if (runningQueue?.size!! < runqueues) {
            runningQueue?.add(tasker)
            pool?.submit(tasker?.m3u8DownRunnable)
            BKLog.d(TAG, "${tasker?.downTask?.name}加入 <运行> 队列")
        } else {
            //判断是否有相同任务，如果有就不添加到准备队列中
            var isExists = false
            for (t in readyQueue?.iterator()!!) {
                if (tasker?.downTask?.vid == t.downTask?.vid) {
                    isExists = true
                    BKLog.d(TAG, "准备队列中存在相同任务：${tasker?.toString()}")
                    break
                }
            }
            if (!isExists) {
                readyQueue?.add(tasker)
            }
            BKLog.d(TAG, "${tasker?.toString()}加入 <准备> 队列")
        }
    }

    @Synchronized
    override fun remove(tasker: M3u8DownTasker?) {
        //任务完成，将运行队列中的该任务移除全部移除
        for (t in runningQueue?.iterator()!!) {
            //将
            if (t.downTask?.vid == tasker?.downTask?.vid) {
                tasker?.m3u8DownRunnable?.isRuning?.set(false)
                runningQueue?.remove(t)
                BKLog.d(TAG, "${t?.toString()} <移出> 运行队列")
            }
        }

        //判断准备队列中是否还有任务
        if (readyQueue?.size!! > 0) {
            val readyTasker = readyQueue?.take()
            enqueue(readyTasker)
        } else {
            BKLog.d(TAG, "<已全部完成> 队列")
        }
    }

    @Deprecated("统一使用下载管理比较好")
    fun remove(url: String) {
        for (tasker in runningQueue?.iterator()!!) {
            if (url == tasker.downTask?.m3u8) {
                tasker.m3u8DownRunnable?.stop()
                runningQueue?.remove(tasker)
                remove(tasker)
                return
            }
        }
        for (tasker in readyQueue?.iterator()!!) {
            if (url == tasker.downTask?.m3u8) {
                readyQueue?.remove(tasker)
                tasker.m3u8DownRunnable?.stop()
                return
            }
        }

    }

    fun removeAll() {
        for (tasker in runningQueue?.iterator()!!) {
            tasker.m3u8DownRunnable?.stop()
            runningQueue?.remove(tasker)
        }
        for (tasker in readyQueue?.iterator()!!) {
            readyQueue?.remove(tasker)
            tasker.m3u8DownRunnable?.stop()
        }
    }

    /**
     * 建造者
     */
    class Builder {
        /**
         * 线程池
         */
        var pool: ExecutorService? = null
        /**
         * 队列运行的最大数量
         */
        var runqueues = 3
        /**
         * 运行的任务队列
         */
        var runningQueue: BlockingQueue<M3u8DownTasker>? = null
        /**
         * 准备的运行队列
         */
        var readyQueue: BlockingQueue<M3u8DownTasker>? = null

        init {
            runqueues = 5
            pool = ThreadPoolExecutor(runqueues, runqueues, 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000))
            runningQueue = LinkedBlockingQueue<M3u8DownTasker>()
            readyQueue = LinkedBlockingQueue<M3u8DownTasker>()
        }
    }


}

fun main(args: Array<String>) {
    val runningQueue = LinkedBlockingQueue<String>()

    runningQueue.add("1")
    runningQueue.add("2")
    runningQueue.add("3")
    runningQueue.add("4")
    runningQueue.offer("9")
    runningQueue.clear()
    runningQueue.add("10")
    for (queue in runningQueue) {
        print(queue + "")
    }
    print(runningQueue.toString())

}