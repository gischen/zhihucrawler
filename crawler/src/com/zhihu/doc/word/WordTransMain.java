package com.zhihu.doc.word;

import com.zhihu.bean.Blogbean;
import com.zhihu.util.Excel;
import com.zhihu.util.FileNameExt;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/4.
 * tika example
 */
public class WordTransMain {

    private static Logger logger = Logger.getLogger(WordTransMain.class);

    public static void main(String[] args) throws Exception{

        Word2Html word2Html = new Word2Html();
        ArrayList<Blogbean> blogs = new ArrayList<Blogbean>();

        //word document location,new excel file location
        String filePath = "d:\\test";
        File folder = new File(filePath);
        //excel file name
        String excelName  = "ArcGIS性能调优";
        //document file keyword
        String keyword = "doc";


        File[] files = FileNameExt.searchFile(folder, keyword);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            logger.info(file.getAbsolutePath() + "\n");
            Blogbean blogbean = word2Html.parseToHTML(file);
            blogs.add(blogbean);
        }

        Excel excel = new Excel();
        excel.exportBlogToExcel(blogs,filePath, excelName+".xls");

        logger.info("======"+folder+" all documents Transform ok! and excel is ok! and excel paht is "+filePath+excelName+".xls");
    }
}
