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
public class WeixinCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(WeixinCrawler.class);
    private Site site = Site.me().setUserAgent("Mozilla/5.0 (compatible; BoxBrowserTest/4.x; Linux) CSSBox/4.x (like Gecko)").setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"NJY4Wl1KfYdQRg1bzQWSKw"};
    static String author = "ArcGIS极客说";

    @Override
    public void process(Page page) {

        if (page != null) {

            page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[1]/h2/text()"));
            logger.info(page.getHtml().xpath("/html/body/div[1]/div[2]/div[2]/div[1]/h2/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[1]"));//有时是div[3],这个需要注意
            logger.info(page.getHtml().xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[2]"));
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

        String startUrl = "https://mp.weixin.qq.com/s/";
        WeixinPipeline WeixinPipeline = new WeixinPipeline();
        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new WeixinCrawler())
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
