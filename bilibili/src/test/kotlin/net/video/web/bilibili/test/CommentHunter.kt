package net.video.web.bilibili.test

import net.video.hunter.bilibili.BilibiliHunter
import net.video.hunter.bilibili.Sort
import org.quickapi.core.Logger
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author chpengzh
 */
//爬取 av2171691 的所有评论
fun main(args: Array<String>) {
    val log = Logger.defaultInstance()
    val comment = BilibiliHunter.getDefault().huntComment(Sort.ORDER_BY_TIME, 2171691L, 1);
    log.i(comment)
    val executors = ArrayList<Future<*>>()
    val service = Executors.newFixedThreadPool(10)
    val pages = comment.data.page.acount / comment.data.page.size;
    for (i in 2..pages) executors.add(service.submit({
        try {
            log.i(BilibiliHunter.getDefault().huntComment(Sort.ORDER_BY_TIME, 2171691L, i.toInt()))
        } catch(ignore: Exception) {
            log.i("page%d hunt fail", i)
        }
    }))
    for (i in 1..pages - 2) executors[i.toInt()].isDone
    service.shutdown()
}