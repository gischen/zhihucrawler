package com.zhihu.util;

import com.zhihu.bean.Blogbean;
import com.zhihu.bean.KBbean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015/8/19.
 */
public class Excel {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Excel.class);

    /**
     * 导出excel
     * @param kbs
     * @param path
     * @param filename
     */
    public void  exportToExcel(List<KBbean> kbs,String path,String filename){
        // 创建一个工作簿
        HSSFWorkbook workBook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workBook.createSheet("解决方案");
        // 创建一个单元格，从0开始
        HSSFRow row = sheet.createRow((short) 0);
        // 构造一个数组设置表头
        HSSFCell cell[] = new HSSFCell[12];
        for (int i = 0; i < 12; i++) {
            cell[i] = row.createCell(i);
        }
        cell[0].setCellValue("编号");
        cell[1].setCellValue("简短问题");
        cell[2].setCellValue("详细描述");
        cell[3].setCellValue("专题");
        cell[4].setCellValue("话题");
        cell[5].setCellValue("问题来源连接");
        cell[6].setCellValue("回答一");
        cell[7].setCellValue("回答二");
        cell[8].setCellValue("回答三");
        cell[9].setCellValue("分类ID");
        cell[10].setCellValue("分享人");
        cell[11].setCellValue("产品名称");

        for (int i = 0; i < kbs.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i+1);
            HSSFCell data[] = new HSSFCell[12];
            for (int j = 0; j < 12; j++) {
                data[j] = dataRow.createCell(j);
            }
            KBbean kBbean =  kbs.get(i);

            //填充excel cell
            data[0].setCellValue(i);//编号
            data[1].setCellValue(kBbean.getTitle());//简短问题
            data[2].setCellValue(kBbean.getDetail().substring(0,kBbean.getDetail().length()>32767?32767:kBbean.getDetail().length()));//详细描述
            data[3].setCellValue("");//专题
            data[4].setCellValue(kBbean.getTag());//话题
            data[5].setCellValue(kBbean.getSource());//问题来源连接
            data[6].setCellValue(kBbean.getContent().substring(0,kBbean.getContent().length()>32767?32767:kBbean.getContent().length()));//回答一
            data[7].setCellValue("");//回答二
            data[8].setCellValue("");//回答三
            data[9].setCellValue("");//分类ID
            data[10].setCellValue("EsriSupport");//分享人
            data[11].setCellValue("");//产品名称
        }

        try {
            File rpDir = new File(path);
            if(!rpDir.exists()) {
                rpDir.mkdir();
            }
            String resultPath = path+"\\"+filename;
            FileOutputStream fos = new FileOutputStream(resultPath);
            // 写入数据，并关闭文件
            workBook.write(fos);
            fos.close();
            logger.info("============Excel created successfully!=================");

        } catch (FileNotFoundException e) {
            logger.error("============write excel occur FileNotFoundException========" + e);
        } catch (IOException e) {
            logger.error("============write excel occur IOException========"+e);
        }
    }

    /**
     * 将blog保存到excel中
     * @param blogs
     * @param path
     * @param filename
     */
    public void  exportBlogToExcel(List<Blogbean> blogs,String path,String filename){

        // 创建一个工作簿
        HSSFWorkbook workBook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workBook.createSheet("博客文章");
        // 创建一个单元格，从0开始
        HSSFRow row = sheet.createRow((short) 0);
        // 构造一个数组设置表头
        HSSFCell cell[] = new HSSFCell[8];
        for (int i = 0; i < 8; i++) {
            cell[i] = row.createCell(i);
        }
        cell[0].setCellValue("编号");
        cell[1].setCellValue("博客标题");
        cell[2].setCellValue("博客内容");
        cell[3].setCellValue("话题");
        cell[4].setCellValue("标签");
        cell[5].setCellValue("博客来源链接");
        cell[6].setCellValue("分享人");
        cell[7].setCellValue("分类");

        for (int i = 0; i < blogs.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i+1);
            HSSFCell data[] = new HSSFCell[8];
            for (int j = 0; j < 8; j++) {
                data[j] = dataRow.createCell(j);
            }
            Blogbean blogbean =  blogs.get(i);

            //填充excel cell
            data[0].setCellValue(i);//编号
            data[1].setCellValue(blogbean.getTitle());//博客标题
            data[2].setCellValue(blogbean.getContent().substring(0, blogbean.getContent().length() > 32767 ? 32767 : blogbean.getContent().length()));//详细描述
            data[3].setCellValue(blogbean.getTopic());//话题
            data[4].setCellValue(blogbean.getTags());//标签
            data[5].setCellValue(blogbean.getSource());//博客来源连接
            data[6].setCellValue(blogbean.getAuthor());//分享人
            data[7].setCellValue(blogbean.getCategory()); //分类
        }

        try {
            File rpDir = new File(path);
            if(!rpDir.exists()) {
                rpDir.mkdir();
            }
            String resultPath = path+"\\"+filename;
            FileOutputStream fos = new FileOutputStream(resultPath);
            // 写入数据，并关闭文件
            workBook.write(fos);
            fos.close();
            logger.info("============Excel created successfully!=================");

        } catch (FileNotFoundException e) {
            logger.error("============write excel occur FileNotFoundException========" + e);
        } catch (IOException e) {
            logger.error("============write excel occur IOException========"+e);
        }
    }
}
