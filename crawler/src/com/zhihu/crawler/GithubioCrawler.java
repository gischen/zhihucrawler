package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.GithubioPipeline;
import com.zhihu.pipeline.IteyePipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

/**
 * 提取githubio建立的博客单页文章内容
 */
public class GithubioCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(GithubioCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"2016/06/01/dev2016/"};
    static String author = "刘峥";

    @Override
    public void process(Page page) {

        if (page != null) {
            String tempStr = getSubStr(page.getUrl().toString(),2).replace("/","");
            page.putField("title", page.getHtml().xpath("//*[@id=\"post-"+tempStr+"\"]/div/header/h1/text()"));
            page.putField("topic", "");
            page.putField("content", page.getHtml().xpath("//*[@id=\"post-"+tempStr+"\"]/div/div[2]"));
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

        String startUrl = "http://oopsliu.github.io/";
        GithubioPipeline githubioPipeline = new GithubioPipeline();
        for (int i = 0; i < articleOkIds.length; i++) {
            Spider.create(new GithubioCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(githubioPipeline)
                    .thread(10)
                    .run();
        }
        ArrayList<Blogbean> blogs = githubioPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1";
        excel.exportBlogToExcel(blogs,filePath,author+".xls");
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
