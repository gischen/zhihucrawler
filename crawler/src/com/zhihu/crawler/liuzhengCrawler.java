package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.GithubioPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * 提取githubio建立的博客单页文章内容
 */
public class liuzhengCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(liuzhengCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"2017/07/mappingapi-intro/","2017/06/arcgis-performance-tuning/","2017/06/engine-gp/","2017/06/arcgis-python-addins/"};
    static String author = "刘峥";

    @Override
    public void process(Page page) {

        if (page != null) {
            String tempStr = getSubStr(page.getUrl().toString(),2).replace("/","");
            page.putField("title", page.getHtml().xpath("//*[@class=entry-title]/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("//*[@class=entry-content]"));
            page.putField("source", page.getUrl());
            page.putField("author", author);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        //http://oopsliu.github.io/2016/06/01/dev2016/

        String startUrl = "http://oopsliu.com/";
        ArrayList<Blogbean> blogs = new ArrayList<Blogbean>();

        for (int i = 0; i < articleOkIds.length; i++) {
            GithubioPipeline githubioPipeline = new GithubioPipeline();
            Spider.create(new liuzhengCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(githubioPipeline)
                    .thread(10)
                    .run();
             blogs.add(githubioPipeline.getBlogbean());
        }

        Excel excel = new Excel();
        String filePath = "d:\\test1";
        excel.exportBlogToExcel(blogs,filePath,author+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }

    private static String getSubStr(String str, int num) {
        String result = "";
        int i = 0;
        while(i < num) {
            int lastFirst = str.lastIndexOf('/');
            result = str.substring(lastFirst) + result;
            str = str.substring(0, lastFirst);
            i++;
        }
        return result.substring(1);
    }
}
