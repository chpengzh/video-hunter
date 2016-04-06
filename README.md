video-hunter
============

写出来玩的一个简单项目, 多线程爬虫的一个demo
用于爬取 bilibili 视屏网站的所有视频信息以及评论信息

dom 解析[jsoup](http://jsoup.org)
restful api 解析[retrofit2](https://github.com/square/retrofit)

单线程环境下使用如下同步函数爬取

```java
BilibiliHunter hunter = BilibiliHunter.getDefault();

//获取页面信息
Bilibili bilibili = hunter.huntAV(); 

//获取评论
Comment comment = hunter.huntComment(Sort.ORDER_BY_TIME, 2171691L/*video aid*/,1/*page*/)
```


