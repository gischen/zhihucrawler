package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.SinaBlogPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * 抓取sina博客单一页面抓取
 */
public class SinaBlogmulPageCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SinaBlogmulPageCrawler.class);
    private String  userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setUserAgent(userAgent);
    static String[] articleOkIds = {"764b1e9d0102z9st","764b1e9d0102z9st"};
//  static String[] articleOkIds = {"764b1e9d0102z9st","764b1e9d0102z6nx","764b1e9d0102z71w","764b1e9d0102z77p","764b1e9d0102z7gz",};
    static String blogOwner = "ENVIIDL技术殿堂";


    @Override
    public void process(Page page) {

        if (page != null) {
            page.putField("title",page.getHtml().xpath("//*[@id=\"articlebody\"]/div[1]/h2/text()"));
            page.putField("topic",page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area\"]/table/tbody/tr/td[2]/a/text()"));
            page.putField("content",page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area2\"]"));
            page.putField("source",page.getUrl());
            page.putField("author",blogOwner);
        }


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String startUrl = "http://blog.sina.com.cn/s/blog_";
        SinaBlogPipeline sinaBlogPipeline = new SinaBlogPipeline();

        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new  SinaBlogmulPageCrawler())
                    .addUrl(startUrl+articleOkIds[i]+".html")
                    .addPipeline(sinaBlogPipeline)
                    .thread(10)
                    .run();
        }

        ArrayList<Blogbean> blogs = sinaBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1\\envi";
        excel.exportBlogToExcel(blogs,filePath,blogOwner+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");




    }
}
