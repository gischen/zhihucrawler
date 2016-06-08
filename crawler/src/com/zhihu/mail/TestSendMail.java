package com.zhihu.mail;

/**
 * Created by Administrator on 2015/7/21.
 */
public class TestSendMail {

    public static void main(String[] args) throws Exception{

        String to = "eweek2@126.com,chenyl@esrichina.com.cn";
        String fromUser = "zhihu@esrichina.com.cn";
        String smtpServer = "smtp.esrichina.com.cn";
        String password = "Esrisuper123";

        MailFacade mailFacade=new MailFacade(smtpServer,fromUser,password,false);

        Mail mail=new Mail();
        mail.setSendAddress(fromUser);
        mail.setInceptAddress(to);
        mail.setTitle("邮件测试");
        mail.setContent("邮件测试");
        mail.attachfile("e:\\2015-07华北上半月.docx");

        boolean sendResult = mailFacade.startSend(mail);
        if(sendResult){
            System.out.print("邮件发送成功！");
        }else{
            System.out.print("邮件发送失败！");
        }
    }
}
