package com.zhihu.pipeline;

import com.zhihu.bean.KBbean;
import com.zhihu.html2bbcode.HTML;
import com.zhihu.util.Filter;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Administrator on 2015/9/6.
 */
public class SupportNewPipeline implements Pipeline {

    private static Logger logger = Logger.getLogger(SupportNewPipeline.class);

    private Properties properties = new Properties();
    String temppath = "";
    String baseurl = "http://support.esri.com/en/knowledgebase/techarticles";
    String errorsrc = "";


    public SupportNewPipeline() {

        //======读取配置文件======//
        String filePath = System.getProperty("user.dir")+"\\conf\\config.properties";
        logger.info("filepath:========"+filePath+"\n");
        properties = new Properties(); //生成properties对象
        try {
            InputStream ins=new BufferedInputStream(new FileInputStream(filePath));
            properties.load(ins);
            ins.close();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Csdn blog pipeline read config error!"+e);
        }

        temppath = properties.getProperty("temppath");
        errorsrc = properties.getProperty("errorsrc");
    }

    ArrayList<KBbean> problems = new ArrayList<KBbean>();
    ArrayList<KBbean> bugs = new ArrayList<KBbean>();
    ArrayList<KBbean> errors = new ArrayList<KBbean>();
    ArrayList<KBbean> howtos = new ArrayList<KBbean>();
    ArrayList<KBbean> faqs = new ArrayList<KBbean>();
    ArrayList<String> kbUrls = new ArrayList<String>();

    @Override
    public void process(ResultItems resultItems, Task task) {

        if (resultItems.get("title") != null) {
            String title = resultItems.get("title").toString();
            if (title != null) {
                kbUrls.add(resultItems.get("sourceUrl").toString());
                int length = title.length();
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("problem")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String con = HTML.newSupporthtml2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                        kBbean.setType("problem");
                        String source = "\n【[b]原文链接[/b]】\n"+ resultItems.get("sourceUrl");
                        kBbean.setSource(source);
                        con = con + source;
                        kBbean.setContent(con);
                    } catch (Exception e) {
                        logger.info(e);
                        e.printStackTrace();
                    }
                    problems.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("how to")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String con = HTML.newSupporthtml2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                        kBbean.setType("howto");
                        String source = "\n【[b]原文链接[/b]】\n"+ resultItems.get("sourceUrl");
                        kBbean.setSource(source);
                        con = con + source;
                        kBbean.setContent(con);
                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    howtos.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("faq")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String con = HTML.newSupporthtml2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                        kBbean.setType("faq");
                        String source = "\n【[b]原文链接[/b]】\n"+ resultItems.get("sourceUrl");
                        kBbean.setSource(source);
                        con = con + source;
                        kBbean.setContent(con);
                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    faqs.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("error")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String con = HTML.newSupporthtml2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                        kBbean.setType("error");
                        String source = "\n【[b]原文链接[/b]】\n"+ resultItems.get("sourceUrl");
                        kBbean.setSource(source);
                        con = con + source;
                        kBbean.setContent(con);
                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    errors.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("bug")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String con = HTML.newSupporthtml2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                        kBbean.setType("bug");
                        String source = "\n\n【[b]原文链接[/b]】\n"+ resultItems.get("sourceUrl");
                        kBbean.setSource(source);
                        con = con + source;
                        kBbean.setContent(con);
                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    bugs.add(kBbean);
                }
            }
        }
    }

    public ArrayList<KBbean> getProblems() {
        return problems;
    }

    public void setProblems(ArrayList<KBbean> problems) {
        this.problems = problems;
    }

    public ArrayList<KBbean> getBugs() {
        return bugs;
    }

    public void setBugs(ArrayList<KBbean> bugs) {
        this.bugs = bugs;
    }

    public ArrayList<KBbean> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<KBbean> errors) {
        this.errors = errors;
    }

    public ArrayList<KBbean> getHowtos() {
        return howtos;
    }

    public void setHowtos(ArrayList<KBbean> howtos) {
        this.howtos = howtos;
    }

    public ArrayList<KBbean> getFaqs() {
        return faqs;
    }

    public void setFaqs(ArrayList<KBbean> faqs) {
        this.faqs = faqs;
    }

    public ArrayList<String> getKbUrls() {
        return kbUrls;
    }

    public void setKbUrls(ArrayList<String> kbUrls) {
        this.kbUrls = kbUrls;
    }
}
