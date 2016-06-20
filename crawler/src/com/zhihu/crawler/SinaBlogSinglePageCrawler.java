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

/**
 * 抓取sina博客单一页面抓取
 */
public class SinaBlogSinglePageCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SinaBlogSinglePageCrawler.class);
    private String  userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setUserAgent(userAgent);

    static String blogOwner = "envi";

    @Override
    public void process(Page page) {

        if(page != null){

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

        String startUrl = "http://blog.sina.com.cn/s/blog_764b1e9d0100vogp.html";
        SinaBlogPipeline sinaBlogPipeline = new SinaBlogPipeline();

        Spider.create(new SinaBlogSinglePageCrawler())
                .addUrl(startUrl)
                .addPipeline(sinaBlogPipeline)
                .thread(10)
                .run();

        ArrayList<Blogbean> blogs = sinaBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "c:\\Users\\Administrator\\Desktop\\TempTest\\suppportcrawler\\";
        excel.exportBlogToExcel(blogs,filePath,blogOwner+".xls");




    }
}
