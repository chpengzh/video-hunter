package net.video.hunter.bilibili

import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author chpengzh
 */
data class Comment(var data: Data) {
    data class Data(var page: Page, var replies: List<Reply>) {
        data class Page(var acount: Long, var size: Long, var num: Long)
        data class Reply(var member: Member, var content: Content, var replies: List<Reply>) {
            data class Member(var mid: Long, var uname: String)
            data class Content(var message: String)
        }
    }
}

data class Bilibili(
        val title: String,
        val aid: Long,
        val cid: Long,
        val tags: List<String>,
        val introduction: String,
        val authorName: String,
        val authorId: Long,
        val createTime: String,

        val url: String = "http://www.bilibiili.com/video/av$aid",
        val update: Long = System.currentTimeMillis()
)

internal class BilibiliHunterImpl : BilibiliHunter {

    private val service = Retrofit.Builder()
            .baseUrl("http://www.bilibili.com")
            .addConverterFactory(GsonConverterFactory.create()).build();

    override fun huntAV(aid: Long): Bilibili? {
        val doc = Jsoup.connect("http://www.bilibili.com/video/av$aid").get()
        //title
        val v_title = doc.title()
        //aid & cid
        val meta = doc.select("#bofqi script").first().childNode(0).toString()
        var code = meta.substring(0, meta.lastIndexOf("\""))
        val codes = code.substring(code.lastIndexOf("\"") + 1).split("&")
        val v_aid: Long = codes.find { it.startsWith("aid=") }!!.substring("aid=".length).toLong()
        val v_cid: Long = codes.find { it.startsWith("cid=") }!!.substring("cid=".length).toLong()
        //tags
        val v_tags = doc.select(".tag-val").map({ it.text() }).toList()
        //introduction
        val intro = doc.select("#v_desc").first().text()
        //author
        val authorName = doc.select(".usname .name").first().text()
        val authorId = doc.select(".r-info .f").first().attr("mid").toLong()
        //create time
        val create = doc.select("time").first().attr("datetime")
        return Bilibili(title = v_title, aid = v_aid, cid = v_cid, tags = v_tags,
                introduction = intro, authorName = authorName, authorId = authorId,
                createTime = create)
    }

    override fun huntComment(sort: Sort, oid: Long, page: Int): Comment {
        return getService(JsonService::class.java).getComment(
                sort = sort.value, oid = oid, page = page
        ).execute().body();
    }

    private fun <T> getService(clazz: Class<T>): T = service.create(clazz)!!
}

internal interface JsonService {
    @GET("http://api.bilibili.com/x/reply")
    fun getComment(
            //默认响应类型
            @Query("type") type: Int = 1,

            //分页排序方式
            @Query("sort") sort: Int = Sort.ORDER_BY_REPLY.value,

            //视屏av号
            @Query("oid") oid: Long,

            //评论页数
            @Query("pn") page: Int = 1
    ): Call<Comment>
}

enum class Sort(val value: Int) {
    ORDER_BY_TIME(0), // 1.默认排序(时间倒序)
    ORDER_BY_REPLY(1), // 2.按回复数排序
    ORDER_BY_SUPPORT(2);// 3.按赞同数排序
}


