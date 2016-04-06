package net.video.web.bilibili.test

import net.video.hunter.bilibili.BilibiliHunter
import org.quickapi.core.Logger
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future

//爬取 av1 - av1000 的所有视频信息
fun main(args: Array<String>) {
    val log = Logger.defaultInstance()
    val executors = ArrayList<Future<*>>()
    val service = Executors.newFixedThreadPool(10)
    for (i in 1..1000L) executors.add(service.submit({
        try {
            val page = BilibiliHunter.getDefault().huntAV(i)
            log.i(page)
        } catch(ignore: Exception) {
            log.i("av%d hunt fail", i)
        }
    }))
    for (i in 0..1000 - 1) executors[i].isDone
    service.shutdown()
}