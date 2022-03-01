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
import java.util.Calendar;
/**
 * 根据分类代码，抓取sina博客站点的部分文章
 */
public class SinaBlogByCategoryCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SinaBlogByCategoryCrawler.class);
    private String  userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000).setUserAgent(userAgent);

    static String[] categories = {"http://blog.sina.com.cn/s/articlelist_1984634525_12_1.html",
                                    "http://blog.sina.com.cn/s/articlelist_1984634525_13_1.html"
                                  };
    static String[] blog = {"1984634525","ENVIIDL技术殿堂","764b1e9d"};

    @Override
    public void process(Page page) {

        //http://blog.sina.com.cn/s/articlelist_2202796977_1_1.html
        String listUrlPattern = "http://blog.sina.com.cn/s/"+"articlelist_"+blog[0]+"_\\d+"+"_\\d+"+".html";

        //http://blog.sina.com.cn/s/blog_834c03b10101cvos.html
        String detailUrlPattern = "^(http://blog.sina.com.cn/s/blog_"+blog[2]+"(\\d{1,}?|\\w{1,}?).html)";

        //待抓取的detail url
        List<String> listUrls = new ArrayList<String>();

        if(page.getHtml().xpath("//*[@id=\"module_928\"]/div[2]/div[1]/div[1]").links().regex(detailUrlPattern).match()) {
            listUrls = page.getHtml().xpath("//*[@id=\"module_928\"]/div[2]/div[1]/div[1]").links().regex(detailUrlPattern).all();
            page.addTargetRequests(listUrls);
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

        SinaBlogPipeline sinaBlogPipeline = new SinaBlogPipeline();
        for (int i = 0; i <categories.length ; i++) {

            Spider.create(new SinaBlogByCategoryCrawler())
                .addUrl(categories[i])
                .addPipeline(sinaBlogPipeline)
                .thread(10)
                .run();
        }
        ArrayList<Blogbean> blogs = sinaBlogPipeline.getBlogs();
        Excel excel = new Excel();
        String filePath = "d:\\test1\\envi";
        excel.exportBlogToExcel(blogs,filePath,blog[0]+Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");
    }
}
