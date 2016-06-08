package com.zhihu.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * Created by Administrator on 2015/9/29.
 */
public class FileUpload {

    private static Logger logger = Logger.getLogger(FileUpload.class);
    private  Properties properties = new Properties();
    private  Auth zhihuAuth = null;
    private  String qiniudomain = "";
    private  String bucket = "";

    public FileUpload() {

        //======读取配置文件=======
        String filePath = System.getProperty("user.dir")+"\\conf\\config.properties";
        logger.info("filepath:========"+filePath+"\n");
        try {
            InputStream ins=new BufferedInputStream(new FileInputStream(filePath));
            properties.load(ins);
            ins.close();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("qiniu read config file error!"+e);
        }

        String accesskey = properties.getProperty("qiniuaccesskey");
        String secretkey = properties.getProperty("qiniusecretkey");
        zhihuAuth = Auth.create(accesskey,secretkey);
        qiniudomain = properties.getProperty("qiniudomain");
        bucket = properties.getProperty("qiniubucket");
    }

    /**
     *
     * @param uploadManager
     * @param bytes
     * @param key
     * @param ps
     * @param mimetype
     * @return  外链地址
     */
    public String fileUpload(UploadManager uploadManager,byte[] bytes,String key,StringMap ps,String mimetype){

        String resulturl = "";
        String expectKey = key;
        StringMap params = ps;
        String mime = mimetype;

        String token = zhihuAuth.uploadToken(bucket, expectKey);
        Response r = null;

        try {
            r = uploadManager.put(bytes, expectKey, token, params, mime, false);
            resulturl = qiniudomain +"/"+key;
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        StringMap map = null;
        try {
            map = r.jsonToMap();
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        //System.out.println(map.get("key"));
        //System.out.println(map.get("hash"));
        //System.out.println(map.get("x:dddd"));
        return resulturl;
    }

    /**
     *
     * @param fileurl
     * @param filekey
     * @return 外链地址
     */
    public  String filefetch(String fileurl,String filekey){

        String resulturl = "";

        if(!fileurl.equals("") && !filekey.equals("")) {
            String url = fileurl;
            String key = filekey;
            try {
                BucketManager bucketManager = new BucketManager(zhihuAuth);
                bucketManager.fetch(url, bucket, key);
                resulturl = qiniudomain + "/" + key;
            } catch (Exception e) {
                System.out.println("========error url:" + fileurl);
                e.printStackTrace();
            }
        }

        return resulturl;
    }
}
