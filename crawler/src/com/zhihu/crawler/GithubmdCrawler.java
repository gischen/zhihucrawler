package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.WeixinPipeline;
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
public class GithubmdCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(GithubmdCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"serverteamCN/TechnicalArticles/blob/master/BigData/%E6%B3%A8%E5%86%8C%E5%A4%A7%E6%95%B0%E6%8D%AE%E6%96%87%E4%BB%B6%E5%85%B1%E4%BA%AB%E6%AD%A5%E9%AA%A4%E5%B8%96.md"};
    static String author = "勾戈雪黎";

    @Override
    public void process(Page page) {

        if (page != null) {

            page.putField("title", page.getHtml().xpath("//*[@class=final-path]/text()").replace(".md",""));
            logger.info(page.getHtml().xpath("/html/body/div[4]/div[1]/div[1]/div[2]/div[4]/div[1]/div[1]/div[2]/h2/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("//*[@class=markdown-body]"));//有时是div[3],这个需要注意
            //logger.info(page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[2]"));
//            logger.info(page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[3]"));
            page.putField("source", page.getUrl());
            page.putField("author", author);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String startUrl = "https://github.com/";
        WeixinPipeline WeixinPipeline = new WeixinPipeline();
        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new GithubmdCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(WeixinPipeline)
                    .thread(10)
                    .run();
        }
        ArrayList<Blogbean> blogs =WeixinPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1";
        excel.exportBlogToExcel(blogs,filePath,author+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }
}
