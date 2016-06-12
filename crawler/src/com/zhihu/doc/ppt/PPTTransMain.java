package com.zhihu.doc.ppt;

import com.zhihu.bean.Blogbean;
import com.zhihu.util.Excel;
import com.zhihu.util.FileNameExt;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/12.
 */
public class PPTTransMain {

    private static Logger logger = Logger.getLogger(PPTTransMain.class);


    public static void main(String[] args) {

        ArrayList<Blogbean> blogs = new ArrayList<Blogbean>();

        //ppt image folder location
        String filePath = "d:/test";
        String folderName = "书籍推荐";
        //excel file name
        String excelName  = folderName;
        //document file keyword
        String keyword = "png";

        File folder = new File(filePath+"/"+folderName);
        Image2bbcode image2bbcode = new Image2bbcode();
        String result = "";

        File[] files = FileNameExt.searchFile(folder, keyword);
        for (int i = 0; i < files.length; i++) {
            int num = i+1;
            File file = new File(filePath+"/"+folderName+"/"+"幻灯片"+num+"."+keyword);
            logger.info(file.getAbsolutePath() + "\n");
            String imageurl = image2bbcode.addFile(file);
            String imagebbcodeurl = "[img]"+imageurl+"[/img]";
            result += imagebbcodeurl+"\n";
        }

        Blogbean blogbean = new Blogbean();
        blogbean.setTitle(folderName);
        blogbean.setContent(result);
        blogbean.setSource("");
        blogbean.setAuthor("ArcGIS知乎");

        blogs.add(blogbean);

        Excel excel = new Excel();
        excel.exportBlogToExcel(blogs,filePath, excelName+".xls");

        logger.info("======"+folder+" all documents Transform ok! and excel is ok! and excel paht is "+filePath+excelName+".xls");
    }
}
