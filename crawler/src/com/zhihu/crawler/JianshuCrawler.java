package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.JianshuPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/9/14.
 */
public class JianshuCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(JianshuCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"bd2a2270e71c","03e8fb9d182f","df923dc4464e","c82fc791cffe"};
    static String author = "石羽";

    @Override
    public void process(Page page) {

        if (page != null) {
            page.putField("title", page.getHtml().xpath("//*[@id=\"flag\"]/div[2]/div[2]/div/h1/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("//*[@id=\"flag\"]/div[2]/div[2]/div/div[4]"));
            page.putField("source", page.getUrl());
            page.putField("author", author);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String startUrl = "http://www.jianshu.com/p/";
        JianshuPipeline jianshuPipeline = new JianshuPipeline();
        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new JianshuCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(jianshuPipeline)
                    .thread(10)
                    .run();
        }
        ArrayList<Blogbean> blogs = jianshuPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "c:\\Users\\Administrator\\Desktop\\TempTest\\suppportcrawler\\";
        excel.exportBlogToExcel(blogs,filePath,author+".xls");
    }
}
