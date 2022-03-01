package com.zhihu.doc.word;

import com.zhihu.bean.Blogbean;
import com.zhihu.util.Excel;
import com.zhihu.util.FileNameExt;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

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
        String filePath = "C:\\Users\\Yue\\Desktop\\xk";
        File folder = new File(filePath);
        //excel file name
        String excelName  = "文章";
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
        excel.exportBlogToExcel(blogs,filePath, excelName+ Calendar.getInstance().get(Calendar.YEAR)+(Calendar.getInstance().get(Calendar.MONTH)+1)+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)+Calendar.getInstance().get(Calendar.SECOND)+".xls");

        logger.info("======"+folder+" all documents Transform ok! and excel is ok! and excel paht is "+filePath+excelName+".xls");
    }
}
