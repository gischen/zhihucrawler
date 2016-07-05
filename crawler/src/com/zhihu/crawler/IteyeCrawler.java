package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.IteyePipeline;
import com.zhihu.pipeline.JianshuPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

/**
 * 提取iteye的单页文章内容
 */
public class IteyeCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(IteyeCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"2283427","2283370","2283371","2283364","2283361"};
    static String author = "华南区技术部";

    @Override
    public void process(Page page) {

        if (page != null) {
            page.putField("title", page.getHtml().xpath("//*[@id=\"main\"]/div[2]/div[1]/h3/a/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("//*[@id=\"blog_content\"]"));
            page.putField("source", page.getUrl());
            page.putField("author", author);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        //http://kevinzeng.iteye.com/blog/2283427

        String startUrl = "http://kevinzeng.iteye.com/blog/";
        IteyePipeline iteyePipeline = new IteyePipeline();
        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new IteyeCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(iteyePipeline)
                    .thread(10)
                    .run();
        }
        ArrayList<Blogbean> blogs = iteyePipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1";
        excel.exportBlogToExcel(blogs,filePath,author+".xls");
    }
}
