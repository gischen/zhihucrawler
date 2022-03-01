package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.CsdnBlogPipeline;
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
 * 抓取csdn某个博客的全部内容
 */
public class CsdnBlogCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(CsdnBlogCrawler.class);
    private Site site = Site.me().addHeader("referer","https://blog.csdn.net").setUserAgent("Mozilla/5.0 (compatible; BoxBrowserTest/4.x; Linux) CSSBox/4.x (like Gecko)").setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {"81330042"};
    static String[] blog = {"liufeng1980423","刘锋"};

    @Override
    public void process(Page page) {

        //http://blog.csdn.net/kikitaMoon/article/list/1
//        String listUrlPattern = "http://blog.csdn.net/"+blog[0]+"/article/list/\\d+";

        //http://blog.csdn.net/kikitamoon/article/details/48239807
//        String detailUrlPattern = "https://blog.csdn.net/"+blog[0]+"/article/details/\\d+";//20180404

        //待抓取的detail url
//        List<String> blogDetailUrls = new ArrayList<String>();//20180404
//        List<String> listUrls = new ArrayList<String>();//20180404


//        if(page.getHtml().links().regex(listUrlPattern).match()){
//            listUrls = page.getHtml().links().regex(listUrlPattern).all();
//            page.addTargetRequests(listUrls);
//        }
//        if(page.getHtml().links().regex(detailUrlPattern).match()){
//            blogDetailUrls = page.getHtml().links().regex(detailUrlPattern).all();
//            page.addTargetRequests(blogDetailUrls);
//        }
//        if(page.getUrl().regex(detailUrlPattern).match()){
//
//            if(articleOkIds.length != 0) {
//                String tempStr = page.getUrl().toString();
//                int start = tempStr.lastIndexOf("/");
//                String articleId = page.getUrl().toString().substring(start + 1);
//                for (int i = 0; i < articleOkIds.length; i++) {
//                    if (articleOkIds[i].equals(articleId)) {
//                        page.putField("title", page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a/text()"));
//                        page.putField("topic", page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span/text()"));
//                        page.putField("content", page.getHtml().css("#article_content").nodes().size() == 0 ? "" : page.getHtml().css("#article_content"));
//                        page.putField("source", page.getUrl());
//                        page.putField("author", blog[1]);
//                    }
//                }
//            }else if(articleOkIds.length == 0)//20180404
        if (page != null) {//20180404
//        {//20180404
                        page.putField("title", page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a/text()"));
                        page.putField("topic", page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span/text()"));
                        page.putField("content", page.getHtml().css("#article_content").nodes().size() == 0 ? "" : page.getHtml().css("#article_content"));
                        page.putField("source", page.getUrl());
                        page.putField("author", blog[1]);

        }

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {


        String startUrl = "http://blog.csdn.net/"+blog[0]+"/article/details/";
        CsdnBlogPipeline csdnBlogPipeline = new CsdnBlogPipeline();
        for (int i = 0; i < articleOkIds.length; i++) {//20180404
            Spider.create(new CsdnBlogCrawler())
                    .addUrl(startUrl+articleOkIds[i])
                    .addPipeline(csdnBlogPipeline)
                    .thread(10)
                    .run();
        }//20180404
        ArrayList<Blogbean> blogs = csdnBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1\\";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }
}
