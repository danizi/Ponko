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

    override fun enqueue(tasker: M3u8DownTasker?) {
        if (runningQueue?.size!! < runqueues) {
            runningQueue?.add(tasker)
            pool?.submit(tasker?.m3u8DownRunnable)
            BKLog.d("${tasker?.downTask?.name}加入 <运行> 队列")
        } else {
            readyQueue?.add(tasker)
            BKLog.d("${tasker?.downTask?.name}加入 <准备> 队列")
        }
    }

    override fun remove(tasker: M3u8DownTasker?) {
        //任务完成，将运行队列中的该任务移除
        runningQueue?.remove(tasker)
        BKLog.d("${tasker?.downTask?.name} <移出> 运行队列")

        //判断准备队列中是否还有任务
        if (readyQueue?.size!! > 0) {
            val readyTasker=readyQueue?.take()
            enqueue(readyTasker)
        } else {
            BKLog.d("准备队列已空，下载队列中的任务已经全部完成")
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
        var runqueues = 1
        /**
         * 运行的任务队列
         */
        var runningQueue: BlockingQueue<M3u8DownTasker>? = null
        /**
         * 准备的运行队列
         */
        var readyQueue: BlockingQueue<M3u8DownTasker>? = null

        init {
            runqueues = 1
            pool = ThreadPoolExecutor(runqueues, runqueues, 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000))
            runningQueue = LinkedBlockingQueue<M3u8DownTasker>()
            readyQueue = LinkedBlockingQueue<M3u8DownTasker>()
        }
    }
}