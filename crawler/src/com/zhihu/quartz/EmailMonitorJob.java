package com.zhihu.quartz;

import com.zhihu.mail.Mail;
import com.zhihu.mail.MailFacade;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2015/7/22.
 */
public class EmailMonitorJob implements Job {

    private static Logger logger = Logger.getLogger(EmailMonitorJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String filePath = System.getProperty("user.dir")+"\\conf\\config.properties";
        Properties p = new Properties();
        logger.info("filepath:========"+filePath+"\n");
        try {
            InputStream ins=new BufferedInputStream(new FileInputStream(filePath));
            p.load(ins);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Zhihu crawler read config file Exception:"+e);
        }

        String monitorUsers = p.getProperty("monitorUsers");
        String fromUser = p.getProperty("fromUser");
        String smtpServer = p.getProperty("SMTPServer");
        String password = p.getProperty("MailPassword");

        MailFacade mailFacade= null;
        try {
            mailFacade = new MailFacade(smtpServer,fromUser,password,false);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Mail facade construct exception:"+e);
        }

        Mail mail=new Mail();
        mail.setSendAddress(fromUser);
        mail.setInceptAddress(monitorUsers);
        mail.setTitle("技术文章自动抓取程序还活着！");
        mail.setContent("技术文章自动抓取程序还活着！请大人放心！");

        boolean sendResult = false;
        try {
            sendResult = mailFacade.startSend(mail);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("==========Send Email Exception :"+e);
        }
        if(sendResult){
            logger.info("============Technical articles auto crawler system send email successfully!");
        }else{
            logger.info("============Technical articles auto crawler system send email occur failure!");
        }
    }
}
