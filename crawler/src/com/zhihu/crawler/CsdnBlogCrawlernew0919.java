package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.CsdnBlogPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.HtmlNode;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 抓取csdn某个博客的全部内容
 */
public class CsdnBlogCrawlernew0919 implements PageProcessor {

    private static Logger logger = Logger.getLogger(CsdnBlogCrawlernew0919.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
//    static String[] articleOkIds = {"79981094","79971460"};
//    static String[] blog = {"ceibake","徐珂"};
    //static String[] articleOkIds = {"102769277"};
    //static String[] blog = {"tuxunxiang","土祥"};
    //static String[] articleOkIds = {"105217075","104839808","104544889"};
    static String[] blog = {"qq_40376439","王璐"};
    //static String[] blog = {"weixin_44616652","敖婧"};
    static String[] articleOkIds = {"110181459"};
    @Override
    public void process(Page page) {

        //http://blog.csdn.net/kikitaMoon/article/list/1
//        String listUrlPattern = "http://blog.csdn.net/"+blog[0]+"/article/list/\\d+";

        //http://blog.csdn.net/kikitamoon/article/details/48239807
        String detailUrlPattern = "https://blog.csdn.net/"+blog[0]+"/article/details/\\d+";
        //String detailUrlPattern = "http://dans.esrichina.com/testpachogn/102769277.htm";
        //待抓取的detail url
        List<String> blogDetailUrls = new ArrayList<String>();
        List<String> listUrls = new ArrayList<String>();


//        if(page.getHtml().links().regex(listUrlPattern).match()){
//            listUrls = page.getHtml().links().regex(listUrlPattern).all();
//            page.addTargetRequests(listUrls);
//        }
        if(page.getHtml().links().regex(detailUrlPattern).match()){
            blogDetailUrls = page.getHtml().links().regex(detailUrlPattern).all();
            page.addTargetRequests(blogDetailUrls);
        }
        if(page.getUrl().regex(detailUrlPattern).match()){

            if(articleOkIds.length != 0) {
                String tempStr = page.getUrl().toString();
                int start = tempStr.lastIndexOf("/");
                String articleId = page.getUrl().toString().substring(start + 1);
                for (int i = 0; i < articleOkIds.length; i++) {
                    if (articleOkIds[i].equals(articleId)) {
                        //System.out.println(page.getHtml().xpath("/html/body/div[1]"));
                        System.out.println(page.getHtml().xpath("/html/body/div[1]/div[1]/main/div[1]/div[1]/div[1]/div[1]/h1/text()"));
                        //page.putField("title", page.getHtml().xpath("//article/h1").nodes().size() == 0 ? "" : page.getHtml().xpath("//article/h1/text()"));
                        page.putField("title", page.getHtml().xpath("/html/body/div[1]/div[1]/main/div[1]/div[1]/div[1]/div[1]/h1/text()"));
                        page.putField("topic", page.getHtml().xpath("//ul[@class=\"article_tags clearfix csdn-tracking-statistics\"]/li/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//ul[@class=\"article_tags clearfix csdn-tracking-statistics\"]/li/a/text()"));
                        page.putField("content", page.getHtml().css("#article_content").nodes().size() == 0 ? "" : page.getHtml().css("#article_content"));
                        page.putField("source", page.getUrl());
                        page.putField("author", blog[1]);
                        us.codecraft.webmagic.selector.HtmlNode d = (HtmlNode) page.getHtml().css("#article_content");

                        //logger.info(page.getHtml());
                        //logger.info(tempStr);
                    }
                }
            }else if(articleOkIds.length == 0){
                page.putField("title", page.getHtml().xpath("//article/h1").nodes().size() == 0 ? "" : page.getHtml().xpath("//article/h1/text()"));
                page.putField("topic", page.getHtml().xpath("//ul[@class=\"article_tags clearfix csdn-tracking-statistics\"]/li/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//ul[@class=\"article_tags clearfix csdn-tracking-statistics\"]/li/a/text()"));
                page.putField("content", page.getHtml().css("#article_content").nodes().size() == 0 ? "" : page.getHtml().css("#article_content"));
                page.putField("source", page.getUrl());
                page.putField("author", blog[1]);


            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {


        String startUrl = "http://blog.csdn.net/"+blog[0]+"/";
        CsdnBlogPipeline csdnBlogPipeline = new CsdnBlogPipeline();

        Spider.create(new CsdnBlogCrawlernew0919())
                .addUrl(startUrl)
                .addPipeline(csdnBlogPipeline)
                .thread(10)
                .run();

        ArrayList<Blogbean> blogs = csdnBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "e:\\test1\\";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }
}
