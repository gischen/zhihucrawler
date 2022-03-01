package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.CnBlogPipeline;
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
 * Created by Administrator on 2015/11/26.
 */
public class CnblogCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(CsdnBlogCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static  String[] articleOkIds = {"9210939"};
    static String[] blog = {"gis-luq","gis_luq"};


    @Override
    public void process(Page page) {

        //http://www.cnblogs.com/myyouthlife/default.html?page=1
        String listUrlPattern = "http://www.cnblogs.com/"+blog[0]+"/default.html\\?page=\\d+";

        //http://www.cnblogs.com/myyouthlife/p/3441926.html
        String detailUrlPattern = "http://www.cnblogs.com/"+blog[0]+"/p/\\d+"+".html";
        //http://www.cnblogs.com/myyouthlife/archive/2012/06/02/2532091.html
        String detailUrlPattern2 = "http://www.cnblogs.com/"+blog[0]+"/archive/\\d+/\\d+/\\d+/\\d+"+".html";


        //待抓取的detail url
        List<String> blogDetailUrls = new ArrayList<String>();
        List<String> listUrls = new ArrayList<String>();

        if(page.getHtml().links().regex(listUrlPattern).match()){
            listUrls = page.getHtml().links().regex(listUrlPattern).all();
            page.addTargetRequests(listUrls);
        }
        if(page.getHtml().links().regex(detailUrlPattern).match()){
            blogDetailUrls = page.getHtml().links().regex(detailUrlPattern).all();
            page.addTargetRequests(blogDetailUrls);
        }

        if(page.getHtml().links().regex(detailUrlPattern2).match()){
            blogDetailUrls = page.getHtml().links().regex(detailUrlPattern2).all();
            page.addTargetRequests(blogDetailUrls);
        }

        if(page.getUrl().regex(detailUrlPattern).match() || page.getUrl().regex(detailUrlPattern2).match()){

            if(articleOkIds.length != 0) {
                String tempStr = page.getUrl().toString();
                int start = tempStr.lastIndexOf("/");
                String articleId = page.getUrl().toString().substring(start + 1);
                for (int i = 0; i < articleOkIds.length; i++) {
                    String  tempHtml =  articleOkIds[i]+".html";
                    if (tempHtml.equals(articleId)) {
                        page.putField("title",page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]").nodes().size() ==0?"":page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/text()"));
                        page.putField("topic",page.getHtml().xpath("//*[@id=\"BlogPostCategory\"]").nodes().size() ==0?"":page.getHtml().xpath("//*[@id=\"BlogPostCategory\"]/a"));
                        page.putField("content",page.getHtml().css("#cnblogs_post_body").nodes().size() ==0?"":page.getHtml().css("#cnblogs_post_body"));
                        page.putField("source",page.getUrl());
                        page.putField("author",blog[1]);
                    }
                }
            }else if(articleOkIds.length == 0){
                page.putField("title",page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]").nodes().size() ==0?"":page.getHtml().xpath("//*[@id=\"cb_post_title_url\"]/text()"));
                page.putField("topic",page.getHtml().xpath("//*[@id=\"BlogPostCategory\"]").nodes().size() ==0?"":page.getHtml().xpath("//*[@id=\"BlogPostCategory\"]/a"));
                page.putField("content",page.getHtml().css("#cnblogs_post_body").nodes().size() ==0?"":page.getHtml().css("#cnblogs_post_body"));
                page.putField("source",page.getUrl());
                page.putField("author",blog[1]);
            }



        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String startUrl = "http://www.cnblogs.com/"+blog[0]+"/";
//        String startUrl = "http://www.cnblogs.com/myyouthlife/p/4156036.html";
        CnBlogPipeline cnBlogPipeline = new CnBlogPipeline();

        Spider.create(new CnblogCrawler())
                .addUrl(startUrl)
                .addPipeline(cnBlogPipeline)
                .thread(10)
                .run();

        ArrayList<Blogbean> blogs = cnBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1\\";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");

    }
}
