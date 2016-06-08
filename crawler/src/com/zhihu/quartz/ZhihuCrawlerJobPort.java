package com.zhihu.quartz;

import com.zhihu.mail.Mail;
import com.zhihu.mail.MailFacade;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Administrator on 2015/7/20.
 */
public class ZhihuCrawlerJobPort {

    private static Logger logger = Logger.getLogger(ZhihuCrawlerJobPort.class);

    public static void main(String[] args) {

        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = new Date();
        String returnstr = DateFormat.format(d);

        MonthJob monthJob = new MonthJob();
        String monthJobName ="MonthJob";

        EmailMonitorJob emailMonitorJob = new EmailMonitorJob();
        String emailMonitorJobName = "emailMonitorJob";

        String filePath = System.getProperty("user.dir")+"\\conf\\config.properties";
        logger.info("filepath:========"+filePath+"\n");
        Properties p = new Properties();
        try {
            InputStream ins=new BufferedInputStream(new FileInputStream(filePath));
            p.load(ins);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("ZhihuExportJobPort read config file Exception:"+e);
        }

        String permonthrule = p.getProperty("permonthrule");
        String emailrule = p.getProperty("emailrule");

        try {

            logger.info(returnstr + "========Run Technical article auto crawler job========\n");
            QuartzManager.addJob(monthJobName, monthJob, permonthrule);

            logger.info(returnstr + "========Run email monitor job========\n");
            QuartzManager.addJob(emailMonitorJobName, emailMonitorJob, emailrule);

        }  catch (Exception e) {
            logger.error("==============Technical article auto crawler job occur Excption :"+e);

            String monitorUsers = p.getProperty("monitorUsers");
            String fromUser = p.getProperty("fromUser");
            String smtpServer = p.getProperty("SMTPServer");
            String password = p.getProperty("MailPassword");

            MailFacade mailFacade= null;
            try {
                mailFacade = new MailFacade(smtpServer,fromUser,password,false);
            } catch (Exception em) {
                //e.printStackTrace();
                logger.error("Mail facade construct exception:"+em);
            }

            Mail mail=new Mail();
            mail.setSendAddress(fromUser);
            mail.setInceptAddress(monitorUsers);
            mail.setTitle("技术文章自动抓取程序出现问题!");
            mail.setContent("技术文章自动抓取程序出现问题!请检查!");

            boolean sendResult = false;
            try {
                sendResult = mailFacade.startSend(mail);
            } catch (Exception es) {
                //e.printStackTrace();
                logger.error("=========Send email occur Exception :"+es);
            }
            if(sendResult){
                logger.info("===========Send email successful!");
            }else{
                logger.info("===========Send email occur failure!");
            }

        }
    }
}
