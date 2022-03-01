package com.zhihu.crawler;

import com.zhihu.bean.KBbean;
import com.zhihu.pipeline.SupportNewPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/6.
 */
public class SupportNewSinglePageCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SupportNewSinglePageCrawler.class);

    private Site site = Site.me().setRetryTimes(5).setSleepTime(1000);

    public int biggetsidint = 0;

    public int getBiggetsidint() {
        return biggetsidint;
    }

    public void setBiggetsidint(int biggetsidint) {
        this.biggetsidint = biggetsidint;
    }

    @Override
    public void process(Page page) {

        if(page != null) {
            String title = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[6]/div/div/div[3]/section[1]/h1/text()").toString().trim();
            int length = title.length();
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("problem")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[4]/div/div[3]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                doc.getElementById("btn-helpful-content").remove();
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("how to")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[4]/div/div[3]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                doc.getElementById("btn-helpful-content").remove();
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("faq")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[4]/div/div[3]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                doc.getElementById("btn-helpful-content").remove();
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("error")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[6]/div/div/div[3]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("text-blue").remove(); //注意，这个有些特殊
                doc.getElementsByClass("source-link").append("<br>");
                doc.getElementById("btn-helpful-content").remove();
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("bug")) {
                page.putField("title", title);
                String contentStr = page.getHtml().xpath("//*[@id=\"wrapper\"]/div[6]/div/div/div[3]").toString();
                Document doc = Jsoup.parse(contentStr);
                doc.getElementsByClass("atricle-headline").remove();
                doc.getElementsByClass("source-link").append("<br>");
                doc.getElementById("btn-helpful-content").remove();
                page.putField("content",doc.toString());
                page.putField("sourceUrl",page.getUrl());
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        //howto http://support.esri.com/technical-article/000004829
        //bug http://support.esri.com/technical-article/000011653
        //error http://support.esri.com/technical-article/000004362
        //faq http://support.esri.com/technical-article/000012741
        //problem http://support.esri.com/technical-article/000012603
        String starturl = "http://support.esri.com/technical-article/000012603";
        SupportNewPipeline supportNewPipeline = new SupportNewPipeline();
        Spider.create(new SupportNewSinglePageCrawler())
                .addUrl(starturl)
                .addPipeline(supportNewPipeline)
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
