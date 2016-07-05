package com.zhihu.crawler;

import com.zhihu.bean.KBbean;
import com.zhihu.pipeline.ExcelPipeline;
import com.zhihu.pipeline.SupportNewPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.downloader.selenium.SupportDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/6.
 */
public class SupportNewCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SupportNewCrawler.class);

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);

    static  String  startUrl = "";

    @Override
    public void process(Page page) {

        //http://support.esri.com/Products/Desktop/arcgis-desktop/arcmap/10-3-1#knowledge-base/technicalarticle?id=000012741
        String urlPattern0 = startUrl+"/"+"technicalarticle\\?id=\\d{9}";

        //存储待抓取的url
        List<String> productUrls0 = new ArrayList<String>();

        if(page.getHtml().xpath("//*[@id=\"kbResults\"]/section[2]").links().regex(urlPattern0).match()){
            productUrls0 =  page.getHtml().xpath("//*[@id=\"kbResults\"]/section[2]").links().regex(urlPattern0).all();
            page.addTargetRequests(productUrls0);
        }
        /*if(page.getUrl().regex(urlPattern0).match()) {
            String title = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]/section[1]/h1/text()").toString().trim();
            int length = title.length();
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("problem")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("how to")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("faq")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("error")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("bug")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("/*//*[@id=\"kbSummaryDiv\"]/div/div[2]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
        }*/
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@: "+productUrls0.size());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        //http://support.esri.com/Products/Desktop/arcgis-desktop/arcmap/10-2-2#knowledge-base
        String[] productType = {"Desktop"};  //"Desktop","Server","Online","Develops","Apps","more-products"
        String[] products = {"arcgis-desktop"};     //"arcgis-desktop"
        String[] apps = {"arcmap"};     //"arcmap","arcgis-pro"
        String[] version= {"10-4-1"};   //"10-3-1","10-4-1","10-2-2"

        String kbUrl = "http://support.esri.com/Products/"+productType[0]+"/"+products[0]+"/"+apps[0]+"/"+version[0]+"#knowledge-base";
        startUrl = kbUrl;
        SupportNewPipeline supportNewPipeline = new SupportNewPipeline();
        Spider.create(new SupportNewCrawler())
                .addUrl(kbUrl)
                .addPipeline(supportNewPipeline)
                .setDownloader(new SupportDownloader("d:\\APP\\chromedriver\\chromedriver220.exe").setSleepTime(3000))
                .thread(10)
                .run();
        logger.info("================================+++++++++++++++++++++++++++================================");

        ArrayList<KBbean> problems =supportNewPipeline.getProblems();
        ArrayList<KBbean> bugs = supportNewPipeline.getBugs();
        ArrayList<KBbean> errors = supportNewPipeline.getErrors();
        ArrayList<KBbean> howtos = supportNewPipeline.getHowtos();
        ArrayList<KBbean> faqs = supportNewPipeline.getFaqs();

        Excel excel = new Excel();
        String filepath = "d:\\test1\\support";
        excel.exportToExcel(problems,filepath,"problem_update.xls");
        excel.exportToExcel(bugs,filepath,"bug_update.xls");
        excel.exportToExcel(errors,filepath,"error_update.xls");
        excel.exportToExcel(faqs, filepath, "faq_update.xls");
        excel.exportToExcel(howtos, filepath, "howto_update.xls");

        //全部已经抓取的技术文章的url
        ArrayList<String> kburls = supportNewPipeline.getKbUrls();
        String urlResults = "";
        for (int i = 0; i <kburls.size() ; i++) {
            String url = kburls.get(i);
            urlResults+=url+"\n";
        }
        logger.info("================================crawler urls:"+ urlResults +"================================");
    }
}
