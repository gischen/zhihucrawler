package com.zhihu.doc.word;

import com.zhihu.html2bbcode.HTML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

/**
 * this file is just a test! It can't work correctly!
 * Created by Administrator on 2016/3/8.
 */
public class PdfTransMain  {


    public static void main(String[] args) throws Exception {

        File file = new File("d:\\ArcGIS for Desktop 10.4许可操作手册.pdf");
        File outfile = new File("d:\\dddd.html");

        // load the PDF file using PDFBox
        PDDocument pdf = PDDocument.load(file);
        // create the DOM parser
        PDFDomTree parser = new PDFDomTree();

        Writer output = new PrintWriter(outfile, "utf-8");
        parser.writeText(pdf, output);
        output.close();

        InputStream inputStream = new FileInputStream(outfile);
        Document doc = Jsoup.parse(inputStream,"utf-8","http://zhihu.esrichina.com.cn");

        String result = HTML.word2html2bbcode(doc.html(),"http://zhihu.esrichina.com.cn","d:\\test","http://zhihu.esrichina.com.cn");

        System.out.println(result);

    }
}
