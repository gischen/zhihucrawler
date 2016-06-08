package com.zhihu.quartz;

import com.zhihu.bean.KBbean;
import com.zhihu.crawler.SupportCrawler;
import com.zhihu.mail.Mail;
import com.zhihu.mail.MailFacade;
import com.zhihu.pipeline.ExcelPipeline;
import com.zhihu.util.Excel;
import com.zhihu.zip.ZipUtil;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/20.
 */
public class MonthJob implements Job{

    private static Logger logger = Logger.getLogger(MonthJob.class);
    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date d = new Date();
    String returnstr = DateFormat.format(d);

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        logger.info(returnstr+"MonthJob");

        try {
            //======读取配置文件=======
            String filePath = System.getProperty("user.dir")+"\\conf\\config.properties";
            logger.info("filepath:========"+filePath+"\n");
            Properties p = new Properties(); //生成properties对象
            try {
                InputStream ins=new BufferedInputStream(new FileInputStream(filePath));
                p.load(ins);
                ins.close();
            } catch (Exception e) {
                //e.printStackTrace();
                logger.error("Zhihu crawler monthJob read config file Exception:"+e);
            }


            Calendar cal = Calendar.getInstance();
            int intyear = cal.get(Calendar.YEAR);
            String year = String.valueOf(intyear);//获取年份

            int intmonth = cal.get(Calendar.MONTH)+1;
            String month= String.valueOf(intmonth);//获取月份



            //===================启动抓取程序======================
            String kbUrl = p.getProperty("kburlstart");

            //获得最大kb id
            String biggestids = p.getProperty("biggestId");//形式为 44128,44985  以逗号分隔的串，最后一个代表为最后一次抓取的最大id
            String[] idarray = biggestids.split(",");
            String biggestid = idarray[idarray.length-1];
            int biggetsidint = Integer.valueOf(biggestid);

            SupportCrawler supportCrawler = new SupportCrawler();
            supportCrawler.setBiggetsidint(biggetsidint);

            ExcelPipeline excelPipeline = new ExcelPipeline();
            Spider.create(supportCrawler)
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
            String filepath = p.getProperty("excelpath");//输出路径

            List<File> srcfile = new ArrayList<File>();

            String problemExcelName  = "problem_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(problems, filepath, problemExcelName);
            srcfile.add(new File(filepath + problemExcelName));

            String bugExcelName = "bug_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(bugs, filepath, bugExcelName);
            srcfile.add(new File(filepath + bugExcelName));

            String errorExcelName = "error_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(errors, filepath, errorExcelName);
            srcfile.add(new File(filepath + errorExcelName));

            String faqExcelName = "faq_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(faqs, filepath, faqExcelName);
            srcfile.add(new File(filepath + faqExcelName));

            String howtoExcelName = "howto_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(howtos, filepath, howtoExcelName);
            srcfile.add(new File(filepath + howtoExcelName));

            String indexExcelName = "index_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(indexs, filepath, indexExcelName);
            srcfile.add(new File(filepath + indexExcelName));

            String installExcelName = "install_"+year+"年"+month+"月"+"_update.xls";
            excel.exportToExcel(installs, filepath, installExcelName);
            srcfile.add(new File(filepath+installExcelName));

            //压缩成一个zip文件
            String ziptag = "KBupdate";
            String dir = filepath + year + "-" + month;
            File rpDir = new File(dir);
            if(!rpDir.exists()) {
                rpDir.mkdir();
            }
            String zipPath = dir+"\\"+year + "-" + month+"-"+ziptag+".zip";
            File zipfile = new File(zipPath);
            ZipUtil.zipFiles(srcfile, zipfile);
            logger.info("===========kb crawler zip file created succssfully!===================");

            //======发邮件======
            String toUsers = p.getProperty("toUsers");
            String fromUser = p.getProperty("fromUser");
            String smtpServer = p.getProperty("SMTPServer");
            String password = p.getProperty("MailPassword");

            MailFacade mailFacade=new MailFacade(smtpServer,fromUser,password,false);

            Mail mail=new Mail();
            mail.setSendAddress(fromUser);
            mail.setInceptAddress(toUsers);
            mail.setTitle(year + "年" + month + "月" + "新更新的技术文章已经抓取成功！");
            mail.setContent(year + "年" + month + "月" + "新更新的技术文章已经生成，"+"详见附件！");
            mail.attachfile(zipPath);

            boolean sendResult = mailFacade.startSend(mail);
            if(sendResult){
                logger.info("===========Solution email sent successfully!");
            }else{
                logger.info("===========Solution email sent occur failure!");
            }

            //全部已经抓取的技术文章的url
            ArrayList<String> kburls = excelPipeline.getKbUrls();
            String urlResults = "";
            int biggersid = 0;
            for (int i = 0; i <kburls.size() ; i++) {
                String url = kburls.get(i);
                int tempId = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1, url.length()));
                if(tempId>biggersid){
                    biggersid = tempId;
                }
                urlResults+=url+"\n";
            }
            logger.info("================================crawler urls:"+ urlResults +"================================");

            //需要把最大的kb id 回写到config文件中
            String ids = p.getProperty("biggestId");
            ids+=","+String.valueOf(biggersid);

            OutputStream fos = new FileOutputStream(filePath);
            p.setProperty("biggestId",ids);
            p.store(fos,"Update biggest id!");
            fos.close();

        }catch (Exception e){
            logger.error("===============Solution auto Export occur Excption:"+e);
        }
    }
}
