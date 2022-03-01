package com.zhihu.pipeline;

import com.zhihu.bean.KBbean;
import com.zhihu.html2bbcode.HTML;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import com.zhihu.util.Filter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Administrator on 2015/9/6.
 */
public class ExcelPipeline implements Pipeline {

    private static Logger logger = Logger.getLogger(ExcelPipeline.class);

    private Properties properties = new Properties();
    String temppath = "";
    String baseurl = "http://support.esri.com/en/knowledgebase/techarticles";
    String errorsrc = "";


    public ExcelPipeline() {

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
    ArrayList<KBbean> installs = new ArrayList<KBbean>();
    ArrayList<KBbean> howtos = new ArrayList<KBbean>();
    ArrayList<KBbean> faqs = new ArrayList<KBbean>();
    ArrayList<KBbean> indexs = new ArrayList<KBbean>();
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
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("bugId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]问题描述[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("description").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]原因[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("cause").toString(),baseurl,temppath,errorsrc);
                        kBbean.setDetail(des);
                        String con = "【[b]解决方案[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("solution").toString(),baseurl,temppath,errorsrc) + "\n";
                        String refStr = HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc);
                        if (!refStr.equals("") && !refStr.contains("Article Rating")) {
                            con = con
                                    + "\n【[b]其它相关参考[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        if (!timeStr.equals("")) {
                            con = con
                                    + "\n【[b]创建及修改时间[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        }

                        kBbean.setType("problem");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "Problem");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
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
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]问题描述[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("summary").toString(),baseurl,temppath,errorsrc) + "\n";
                        kBbean.setDetail(des);

                        String con = "【[b]解决方案[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("procedure").toString(),baseurl,temppath,errorsrc) + "\n";
                        String refStr = HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc);
                        if (!refStr.equals("") && !refStr.contains("Article Rating")) {
                            con = con
                                    + "\n【[b]其它相关参考[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        if (!timeStr.equals("")) {
                            con = con
                                    + "\n【[b]创建及修改时间[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        }

                        kBbean.setType("howto");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "HowTo");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
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
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]问题描述[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("question").toString(),baseurl,temppath,errorsrc) + "\n";
                        kBbean.setDetail(des);
                        String con = "【[b]解决方案[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("answer").toString(),baseurl,temppath,errorsrc) + "\n";
                        String refStr = HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc);
                        if (!refStr.equals("") && !refStr.contains("Article Rating")) {
                            con = con
                                    + "\n【[b]其它相关参考[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        if (!timeStr.equals("")) {
                            con = con
                                    + "\n【[b]创建及修改时间[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        }

                        kBbean.setType("faq");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "FAQ");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
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
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]错误信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("errormessage").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]错误原因[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("cause").toString(),baseurl,temppath,errorsrc);
                        kBbean.setDetail(des);
                        String con = "";
                        String solutionStr = HTML.html2bbcode(resultItems.get("solution").toString(),baseurl,temppath,errorsrc);

                        if (!solutionStr.equals("") && !solutionStr.toLowerCase().equals("none")) {
                            con = con + "【[b]解决方案[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("solution").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String refStr = HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc);
                        if (!refStr.equals("") && !refStr.contains("Article Rating")) {
                            con = con
                                    + "\n【[b]其它相关参考[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        if (!timeStr.equals("")) {
                            con = con
                                    + "\n【[b]创建及修改时间[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        }

                        kBbean.setType("error");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "Error");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
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
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("bugId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]BUG描述[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("description").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]BUG原因[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("cause").toString(),baseurl,temppath,errorsrc);
                        kBbean.setDetail(des);
                        String con = "";
                        String workroundStr = HTML.html2bbcode(resultItems.get("workround").toString(),baseurl,temppath,errorsrc);

                        if (!workroundStr.equals("") && !workroundStr.toLowerCase().equals("none")) {
                            con = con + "【[b]解决方案[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("workround").toString(),baseurl,temppath,errorsrc) + "\n";
                            String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                            if (!timeStr.equals("")) {
                                con = con
                                        + "\n【[b]创建及修改时间[/b]】\n"
                                        + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                            }
                        }
                        kBbean.setType("bug");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "Bug");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
                        kBbean.setSource(source);

                        con = con + source;
                        kBbean.setContent(con);
                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    bugs.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("install")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]系统需求[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("requirements").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]准备工作[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("beforebeginning").toString(),baseurl,temppath,errorsrc) + "\n";
                        kBbean.setDetail(des);

                        String con = "【[b]安装过程[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("procedure").toString(),baseurl,temppath,errorsrc) + "\n";
                        String refStr = HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc);
                        if (!refStr.equals("") && !refStr.contains("Article Rating")) {
                            con = con
                                    + "\n【[b]其它相关参考[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("relatedinfo").toString(),baseurl,temppath,errorsrc) + "\n";
                        }
                        String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        if (!timeStr.equals("")) {
                            con = con
                                    + "\n【[b]创建及修改时间[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                        }

                        kBbean.setType("install");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "Install");
                        String source = "\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
                        kBbean.setSource(source);

                        con = con + source;
                        kBbean.setContent(con);

                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    installs.add(kBbean);
                }
                if (title.substring(0, length < 10 ? length : 10).toLowerCase().contains("index")) {
                    KBbean kBbean = new KBbean();
                    kBbean.setTitle(title);
                    try {
                        String des = "【[b]相关信息[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("articleId").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("software").toString(),baseurl,temppath,errorsrc) + "\n"
                                + HTML.html2bbcode(resultItems.get("platform").toString(),baseurl,temppath,errorsrc) + "\n"
                                + "\n【[b]总结[/b]】\n"
                                + HTML.html2bbcode(resultItems.get("summary").toString(),baseurl,temppath,errorsrc) + "\n";
                        kBbean.setDetail(des);

                        String con = "";
                        String topicStr = HTML.html2bbcode(resultItems.get("topic").toString(),baseurl,temppath,errorsrc);

                        if (!topicStr.equals("")) {
                            con = con + "【[b]相关专题[/b]】\n"
                                    + HTML.html2bbcode(resultItems.get("topic").toString(),baseurl,temppath,errorsrc) + "\n";
                            String timeStr = HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                            if (!timeStr.equals("")) {
                                con = con
                                        + "\n【[b]创建及修改时间[/b]】\n"
                                        + HTML.html2bbcode(resultItems.get("createData").toString(),baseurl,temppath,errorsrc);
                            }
                        }

                        kBbean.setType("index");
                        String tabStr = Filter.filter(resultItems.get("software").toString());
                        kBbean.setTag(tabStr + "Index");
                        String source = "\n\n【[b]原文链接[/b]】\n"
                                + resultItems.get("sourceUrl");
                        kBbean.setSource(source);

                        con = con + source;
                        kBbean.setContent(con);

                    }catch (Exception e){
                        logger.info(e);
                        e.printStackTrace();
                    }
                    indexs.add(kBbean);
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

    public ArrayList<KBbean> getInstalls() {
        return installs;
    }

    public void setInstalls(ArrayList<KBbean> installs) {
        this.installs = installs;
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

    public ArrayList<KBbean> getIndexs() {
        return indexs;
    }

    public void setIndexs(ArrayList<KBbean> indexs) {
        this.indexs = indexs;
    }

    public ArrayList<String> getKbUrls() {
        return kbUrls;
    }

    public void setKbUrls(ArrayList<String> kbUrls) {
        this.kbUrls = kbUrls;
    }
}
