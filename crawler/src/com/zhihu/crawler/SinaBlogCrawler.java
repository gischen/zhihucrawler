package com.zhihu.crawler;

import com.zhihu.bean.Blogbean;
import com.zhihu.pipeline.SinaBlogPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/14.
 */
public class SinaBlogCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SinaBlogCrawler.class);
    private String  userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setUserAgent(userAgent);

    static String[] blog = {"2202796977","刘卓颖","834c03b1"};
    //static String[] blog = {"1984634525","ENVI/IDL","764b1e9d"};

    @Override
    public void process(Page page) {

        //http://blog.sina.com.cn/s/articlelist_2202796977_1_1.html
        String listUrlPattern = "http://blog.sina.com.cn/s/"+"articlelist_"+blog[0]+"_\\d+"+"_\\d+"+".html";

        //http://blog.sina.com.cn/s/blog_834c03b10101cvos.html
        String detailUrlPattern = "^(http://blog.sina.com.cn/s/blog_"+blog[2]+"(\\d{1,}?|\\w{1,}?).html)";

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
        if(page.getUrl().regex(detailUrlPattern).match()){

            page.putField("title",page.getHtml().xpath("//*[@id=\"articlebody\"]/div[1]/h2/text()"));
            page.putField("topic",page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area\"]/table/tbody/tr/td[2]/a/text()"));
            page.putField("content",page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area2\"]"));
            page.putField("source",page.getUrl());
            page.putField("author",blog[1]);

        }

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {

        String startUrl = "http://blog.sina.com.cn/u/"+blog[0]+"/";
        SinaBlogPipeline sinaBlogPipeline = new SinaBlogPipeline();

        Spider.create(new SinaBlogCrawler())
                .addUrl(startUrl)
                .addPipeline(sinaBlogPipeline)
                .thread(10)
                .run();

        ArrayList<Blogbean> blogs = sinaBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "c:\\Users\\Administrator\\Desktop\\TempTest\\suppportcrawler\\";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+".xls");




    }
}
