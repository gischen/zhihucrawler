package com.zhihu.mail;

/**
 * Created by Administrator on 2015/7/21.
 */

import java.util.Date;
import java.util.Vector;

public class Mail {

    private String title; // 主题
    private String inceptAddress; //收件人地址
    private String sendAddress;  //发件人地址
    private String content; // 邮件正文
    private Vector file=null; //附件
    private Date sendTime; // 发送时间

    public Mail(){
        title="";
        inceptAddress="";
        sendAddress="";
        content="";
        file=new Vector();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Vector getFile() {
        return file;
    }

    public void setFile(Vector file) {
        this.file = file;
    }

    public String getInceptAddress() {
        return inceptAddress;
    }

    public void setInceptAddress(String inceptAddress) {
        this.inceptAddress = inceptAddress;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //该方法用于收集附件名
    public void attachfile(String fname){
        file.addElement(fname);
    }

}