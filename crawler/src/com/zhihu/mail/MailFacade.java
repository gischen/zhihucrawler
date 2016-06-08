package com.zhihu.mail;

/**
 * Created by Administrator on 2015/7/21.
 */

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

public class MailFacade {

    private static Logger logger = Logger.getLogger(MailFacade.class);
    private  String SMTPServer = "";
    private  String MailUserName = "";
    private  String MailPassword = "";
    private  boolean SendDebug = true;


    public MailFacade(String SMTPServer,String mailUserName,String mailPassword,boolean senddebug) throws Exception {
        this.SMTPServer = SMTPServer;
        this.MailUserName = mailUserName;
        this.MailPassword = mailPassword;
        this.SendDebug = senddebug;
    }

    /**
     * 发送邮件方法
     * 接收一个Mail对象（封装邮件）
     */
    public boolean startSend(Mail mail) throws Exception {

        //创建Properties对象
        Properties props = System.getProperties();

        //创建信件服务器
        props.put("mail.debug", new Boolean(SendDebug).toString());
        props.put("mail.smtp.host", SMTPServer);
        props.put("mail.smtp.auth", "true");//执行SMTP验证


        MyAuthenticator auth = new MyAuthenticator(MailUserName, MailPassword);

        //得到默认的对话对象
        Session session = Session.getDefaultInstance(props, auth);
        try {
            //创建一个消息，并初始化该消息的各项元素
            MimeMessage msg = new MimeMessage(session);

            //发送人地址
            msg.setFrom(new InternetAddress(mail.getSendAddress()));

            //收件人地址
            InternetAddress[] address = new InternetAddress().parse(mail.getInceptAddress());
            msg.setRecipients(Message.RecipientType.TO, address);

            //设置主题
            msg.setSubject(mail.getTitle().trim());


            //后面的BodyPart将加入到此处创建的Multipart中
            Multipart mp = new MimeMultipart();


            //邮件正文
            MimeBodyPart mbpText = new MimeBodyPart();
            mbpText.setContent(mail.getContent(), "text/html;charset=gb2312");
            mp.addBodyPart(mbpText);

            //利用枚举器方便的遍历集合
            Enumeration efile = mail.getFile().elements();

            //检查序列中是否还有更多的对象
            String filename = "";
            while (efile.hasMoreElements()) {
                MimeBodyPart mbp = new MimeBodyPart();
                //选择出每一个附件名
                filename = efile.nextElement().toString();

                //得到数据源
                FileDataSource fds = new FileDataSource(filename);
                //得到附件本身并至入BodyPart
                mbp.setDataHandler(new DataHandler(fds));

                //得到文件名同样至入BodyPart(同时处理中文文件名)
                String fname = MimeUtility.encodeWord(fds.getName(), "GB2312", null);
                mbp.setFileName(fname);
                mp.addBodyPart(mbp);
            }
            //移走集合中的所有元素
            mail.getFile().removeAllElements();


            //Multipart加入到信件
            msg.setContent(mp);


            //设置信件头的发送日期
            msg.setSentDate(new Date());

            //发送信件
            Transport.send(msg);

        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                //ex.printStackTrace();
                logger.error("=======Send Email excption :"+ex);
            }
            return false;
        }
        return true;  //发送成功
    }
}
