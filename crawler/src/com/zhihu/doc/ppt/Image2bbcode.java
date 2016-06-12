package com.zhihu.doc.ppt;

import com.qiniu.storage.UploadManager;
import com.zhihu.qiniu.FileUpload;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Administrator on 2016/6/12.
 */
public class Image2bbcode {

    private static Logger logger = Logger.getLogger(Image2bbcode.class);
    private Properties properties = new Properties();
    private FileUpload fu = new FileUpload();
    UploadManager uploadManager = new UploadManager();
    String temppath = "";
    String errorsrc = "";

    public Image2bbcode() {

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
            logger.error("word2html2bbcode read config error!"+e);
        }

        temppath = properties.getProperty("temppath");
        errorsrc = properties.getProperty("errorsrc");
    }

    public String addFile(File file){

        String fileName = UUID.randomUUID().toString()+"_ppt"+".png";
        String imageurl = "";

        try {
            InputStream in = new FileInputStream(file);
            byte b[]=new byte[(int)file.length()];
            in.read(b);
            in.close();
            imageurl = fu.fileUpload(uploadManager, b, fileName, null, "image/png");
            logger.info("=====ppt image url:"+imageurl);

        }catch (Exception e){
            e.printStackTrace();
        }
        return imageurl;
    }
}
