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
import java.util.Calendar;
/**
 * 提取简书的单页文章内容
 */
public class JianshuCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(JianshuCrawler.class);
    private Site site = Site.me().setUserAgent("Mozilla/5.0 (compatible; BoxBrowserTest/4.x; Linux) CSSBox/4.x (like Gecko)").setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"f29dd8ca5560"};
    static String author = "许丹石";

    @Override
    public void process(Page page) {

        if (page != null) {
            System.out.println(page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/h1/text()"));
            //System.out.println(page.getHtml().xpath("/html/body/div[1]/div[1]/div[0]/div[0]/section[0]/h1/text()"));
            System.out.println(page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/article[1]"));
            //page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/h1/text()"));
            //page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/h1/text()"));//2020 MD
            page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/h1/text()"));//2021fuwenben
            //page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/h1/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/article[1]"));//2021fuwenben
            //page.putField("content", page.getHtml().xpath("/html/body/div[1]/div[1]/div[1]/div[1]/section[1]/article[1]"));//2020 MD
            //page.putField("content", page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[2]/div[1]"));
            page.putField("source", page.getUrl());
            page.putField("author", author);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String startUrl = "https://www.jianshu.com/p/";
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
        String filePath = "d:\\test1";
        excel.exportBlogToExcel(blogs,filePath,author+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }
}
