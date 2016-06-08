package com.zhihu.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Administrator on 2015/7/21.
 */
public class MyAuthenticator extends Authenticator {
    private String strUser;
    private String strPwd;
    public MyAuthenticator(String user, String password) {
        this.strUser = user;
        this.strPwd = password;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(strUser, strPwd);
    }
}
