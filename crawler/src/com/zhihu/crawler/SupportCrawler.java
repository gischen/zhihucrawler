package com.zhihu.crawler;

import com.zhihu.bean.KBbean;
import com.zhihu.pipeline.ExcelPipeline;
import com.zhihu.util.Excel;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/6.
 */
public class SupportCrawler implements PageProcessor {

    private static Logger logger = Logger.getLogger(SupportCrawler.class);

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

        //http://support.esri.com/en/knowledgebase/techarticles/browse/productid/220
        String urlPattern0 = "http://support.esri.com/en/knowledgebase/techarticles/browse/productid/\\d+";

        //http://support.esri.com/en/knowledgebase/techarticles/browse/productID/160/productFamily/2003
        String urlPattern00 = "http://support.esri.com/en/knowledgebase/techarticles/browse/productID/\\d+/productFamily/\\d+";

        //http://support.esri.com/en/knowledgebase/techarticles/browse/productid/39/productfamily/299/productVersions/63,62,61
        String urlPattern1 = "http://support.esri.com/en/knowledgebase/techarticles/browse/productid/\\d+/productfamily/\\d+/productVersions/[\\d{3},]+";

        //http://support.esri.com/en/knowledgebase/techarticles/detail/22694
        String urlPattern2 = "http://support.esri.com/en/knowledgebase/techarticles/detail/\\d{5}";

        //存储待抓取的url
        List<String> productUrls0 = new ArrayList<String>();
        List<String> productUrls1 = new ArrayList<String>();
        List<String> productUrls2 = new ArrayList<String>();

        //最终要抓取的url列表
        List<String> objectUrls = new ArrayList<String>();

        if(page.getHtml().links().regex(urlPattern0).match()){
            productUrls0 =  page.getHtml().links().regex(urlPattern0).all();
            page.addTargetRequests(productUrls0);
        }
        if(page.getHtml().links().regex(urlPattern00).match()){
            productUrls1 =  page.getHtml().links().regex(urlPattern00).all();
            page.addTargetRequests(productUrls1);
        }
        if (page.getHtml().links().regex(urlPattern1).match()){
            productUrls2 = page.getHtml().links().regex(urlPattern1).all();
            page.addTargetRequests(productUrls2);
        }
        if(page.getHtml().links().regex(urlPattern2).match()) {
            String tempStr = page.getHtml().links().regex(urlPattern2).toString();
            int tempId = Integer.valueOf(tempStr.substring(tempStr.lastIndexOf("/")+1,tempStr.length()));
            if(tempId>biggetsidint){
                page.addTargetRequest(tempStr);
            }
        }

        if(page.getUrl().regex(urlPattern2).match()) {
            String title = page.getHtml().xpath("/html/body/div[3]/div[2]/h2/text()").toString().trim();
            int length = title.length();
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("problem")) {
                //http://support.esri.com/en/knowledgebase/techarticles/detail/43454
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("bugId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[4]"));
                page.putField("description", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("cause", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("solution", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]")); //此处会有部分文章为空  如44514
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("howto")) {

                //http://support.esri.com/en/knowledgebase/techarticles/detail/40006
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));

                page.putField("summary", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("procedure", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]/ul").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]/ul"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("faq")) {
                //http://support.esri.com/en/knowledgebase/techarticles/detail/40057
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));

                page.putField("question", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("answer", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("error")) {
                //http://support.esri.com/en/knowledgebase/techarticles/detail/40063
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));

                page.putField("errormessage", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("cause", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("solution", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]"));//此处会有部分文章报错，如41013
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("bug")) {

                //http://support.esri.com/en/knowledgebase/techarticles/detail/40015
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("bugId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[4]"));
                page.putField("description", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));

                page.putField("cause", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("workround", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]"));
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("install")) {

                //http://support.esri.com/en/knowledgebase/techarticles/detail/43938
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));

                page.putField("requirements", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("beforebeginning", page.getHtml().xpath("/html/body/div[3]/div[2]/div[2]"));
                page.putField("procedure", page.getHtml().xpath("/html/body/div[3]/div[2]/div[3]"));
                page.putField("relatedinfo", page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[4]/ul"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
            if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("index")) {
                //http://support.esri.com/en/knowledgebase/techarticles/detail/40565
                page.putField("title", title);
                page.putField("articleId", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[1]"));
                page.putField("software", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[2]"));
                page.putField("platform", page.getHtml().xpath("/html/body/div[3]/div[2]/table/tbody/tr[3]"));

                page.putField("summary", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]"));
                page.putField("topic", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]/div[4]/ul").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]/div[4]/ul"));
                page.putField("createData", page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]/p").nodes().size()==0?"":page.getHtml().xpath("/html/body/div[3]/div[2]/div[1]/p"));
                page.putField("sourceUrl",page.getUrl());
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        String kbUrl = "http://support.esri.com/en/knowledgebase/techarticles";
        ExcelPipeline excelPipeline = new ExcelPipeline();
        Spider.create(new SupportCrawler())
                .addUrl(kbUrl)
                .addPipeline(excelPipeline)
                .thread(10)
                .run();
        logger.info("================================+++++++++++++++++++++++++++================================");

        ArrayList<KBbean> problems =excelPipeline.getProblems();
        ArrayList<KBbean> bugs = excelPipeline.getBugs();
        ArrayList<KBbean> errors = excelPipeline.getErrors();
        ArrayList<KBbean> installs = excelPipeline.getInstalls();
        ArrayList<KBbean> howtos = excelPipeline.getHowtos();
        ArrayList<KBbean> faqs = excelPipeline.getFaqs();
        ArrayList<KBbean> indexs = excelPipeline.getIndexs();

        Excel excel = new Excel();
        String filepath = "c:\\Users\\Administrator\\Desktop\\TempTest\\suppportcrawler\\";
        excel.exportToExcel(problems,filepath,"problem_update.xls");
        excel.exportToExcel(bugs,filepath,"bug_update.xls");
        excel.exportToExcel(errors,filepath,"error_update.xls");
        excel.exportToExcel(faqs, filepath, "faq_update.xls");
        excel.exportToExcel(howtos, filepath, "howto_update.xls");
        excel.exportToExcel(indexs, filepath, "index_update.xls");
        excel.exportToExcel(installs, filepath, "install_update.xls");

        //全部已经抓取的技术文章的url
        ArrayList<String> kburls = excelPipeline.getKbUrls();
        String urlResults = "";
        for (int i = 0; i <kburls.size() ; i++) {
            String url = kburls.get(i);
            urlResults+=url+"\n";
        }
        logger.info("================================crawler urls:"+ urlResults +"================================");
    }
}
