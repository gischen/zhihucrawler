package com.zhihu.pipeline;

import com.zhihu.bean.Blogbean;
import com.zhihu.html2bbcode.HTML;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Administrator on 2015/9/14.
 */
public class IteyePipeline implements Pipeline {

    private static Logger logger = Logger.getLogger(IteyePipeline.class);
    private Properties properties = new Properties();
    String temppath = "";
    String baseurl = "http://blog.csdn.net/";
    String imgparas = "";
    String errorsrc = "";


    public IteyePipeline() {

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

    public String getTemppath() {
        return temppath;
    }

    public void setTemppath(String temppath) {
        this.temppath = temppath;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    ArrayList<Blogbean> blogs = new ArrayList<Blogbean>();

    public ArrayList<Blogbean> getBlogs() {
        return blogs;
    }

    public void setBlogs(ArrayList<Blogbean> blogs) {
        this.blogs = blogs;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        if(resultItems.get("title") != null){
            String title = resultItems.get("title").toString();
            if (title != null) {
                Blogbean blogbean = new Blogbean();
                blogbean.setTitle(title);
                try {
                    String topic = resultItems.get("topic").toString();
                    blogbean.setTopic(topic);

                    String content = HTML.html2bbcode(resultItems.get("content").toString(),baseurl,temppath,errorsrc);
                    String articleSource = resultItems.get("source").toString();
                    content += "\n\n[b]文章来源：[/b]"+"[url="+articleSource+"]"+articleSource+"[/url]";
                    blogbean.setContent(content);
                    blogbean.setSource(resultItems.get("source").toString());
                    blogbean.setAuthor(resultItems.get("author").toString());

                }catch (Exception e){
                    logger.info(e);
                    e.printStackTrace();
                }
                blogs.add(blogbean);
            }
        }
    }
}
