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

/**
 * 通过指定分类代码，提取分类下的所有文章
 */
public class CsdnBlogByCategoryCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(CsdnBlogByCategoryCrawler.class);
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);
    static String[] articleOkIds = {}; //需要特殊提取的blog id
    static String[] blog = {"esrichinacd","西南区技术部"};
    static String  blogNickName = "esricd"; //有的是有两个名字
    static String[] categories = {"1122220"};

    @Override
    public void process(Page page) {

        //category list
        String categorylistPattern = "http://blog.csdn.net/" + blogNickName + "/article/category/\\d+/\\d+";
        //http://blog.csdn.net/kikitamoon/article/details/48239807
        String detailUrlPattern = "http://blog.csdn.net/"+blog[0]+"/article/details/\\d+";

        //待抓取的detail url
        List<String> listUrls = new ArrayList<String>();
        List<String> categorylistUrls = new ArrayList<String>();

        if(page.getHtml().links().regex(categorylistPattern).match()){
            categorylistUrls = page.getHtml().links().regex(categorylistPattern).all();
            page.addTargetRequests(categorylistUrls);
        }
        if(page.getHtml().xpath("//*[@id=\"article_list\"]").links().regex(detailUrlPattern).match()) {
            listUrls = page.getHtml().xpath("//*[@id=\"article_list\"]").links().regex(detailUrlPattern).all();
            page.addTargetRequests(listUrls);
        }

        if(page.getUrl().regex(detailUrlPattern).match()){

            if(articleOkIds.length != 0) {
                String tempStr = page.getUrl().toString();
                int start = tempStr.lastIndexOf("/");
                String articleId = page.getUrl().toString().substring(start + 1);
                for (int i = 0; i < articleOkIds.length; i++) {
                    if (articleOkIds[i].equals(articleId)) {
                        page.putField("title", page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a/text()"));
                        page.putField("topic", page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span/text()"));
                        page.putField("content", page.getHtml().css("#article_content").nodes().size() == 0 ? "" : page.getHtml().css("#article_content"));
                        page.putField("source", page.getUrl());
                        page.putField("author", blog[1]);
                    }
                }
            }else if(articleOkIds.length == 0){
                page.putField("title", page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[1]/h1/span/a/text()"));
                page.putField("topic", page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span").nodes().size() == 0 ? "" : page.getHtml().xpath("//*[@id=\"article_details\"]/div[3]/div[2]/label/span/text()"));
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

        CsdnBlogPipeline csdnBlogPipeline = new CsdnBlogPipeline();
        for (int i = 0; i < categories.length; i++) {

            //http://blog.csdn.net/esrichinacd/article/category/1306333
            String categoryStr = "http://blog.csdn.net/" + blog[0] + "/article/category/" + categories[i];
            Spider.create(new CsdnBlogByCategoryCrawler())
                    .addUrl(categoryStr)
                    .addPipeline(csdnBlogPipeline)
                    .thread(10)
                    .run();
        }
        ArrayList<Blogbean> blogs = csdnBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1\\";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+".xls");
    }
}
