package com.zhihu.test;

import com.qiniu.storage.UploadManager;
import com.zhihu.html2bbcode.HTML;
import com.zhihu.qiniu.FileUpload;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/16.
 */
public class HtmltagMain {

    public static void main(String[] args) {

        try{
            String  codetag = "c:\\Users\\Administrator\\Desktop\\TempTest\\htmltagtest\\aaaaa.html";
            File input = new File(codetag);
            Document doc = Jsoup.parse(input,"utf-8","http://blog.csdn.net/");
            Elements content = doc.select("#article_details");
            String contentStr = content.outerHtml();

            html2bbcode(contentStr);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void html2bbcode(String contentStr) {
        String basictags[] = new String[]{"a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup", "u", "ul"};

        String additionaltags[] = {"h1","h2","h3","h4","h5","h6","img"};
        String tabletags[] = {"table","thead","tbody","tfoot","tr","td"};

        String baseUri = "http://blog.csdn.net/";

        Whitelist whitelist = new Whitelist()
                .addTags(basictags).addTags(additionaltags)
                .addAttributes("a", new String[]{"href"})
                .addAttributes("img",new String[]{"src"})
                .addAttributes("blockquote", new String[]{"cite"})
                .addAttributes("q", new String[]{"cite"})
                .addProtocols("a", "href", new String[]{"ftp", "http", "https", "mailto"})
                .addProtocols("img", "src", new String[]{"http", "https"})
                .addProtocols("blockquote", "cite", new String[]{"http", "https"})
                .addProtocols("cite", "cite", new String[]{"http", "https"})

                ;
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        String imgepraras = "?imageView2/0/w/600/q/60";
        String errorsrc = "http://7xn3yv.com1.z0.glb.clouddn.com/404.png";

        //选择出table，然后替换成img标签
        Document doc = Jsoup.parse(contentStr);

        Elements tables = doc.getElementsByTag("table");

        FileUpload fu = new FileUpload();
        UploadManager uploadManager = new UploadManager();

        for (int i = 0; i < tables.size() ; i++) {
            Element table = tables.get(i);

            final HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
            Dimension dimension = new Dimension();
            dimension.setSize(600, 400);
            imageGenerator.setSize(dimension);
            imageGenerator.loadHtml(table.outerHtml());

            String filePath = "c:\\Users\\Administrator\\Desktop\\TempTest\\htmltagtest\\";
            String fileName = UUID.randomUUID().toString()+".png";
            imageGenerator.saveAsImage(filePath + fileName);

            String imageurl = "";
            try {
                File file = new File(filePath+fileName);
                InputStream in = new FileInputStream(file);
                byte b[]=new byte[(int)file.length()];
                in.read(b);
                in.close();
                imageurl = fu.fileUpload(uploadManager, b, fileName, null, "image/png");
            }catch (Exception e){
                e.printStackTrace();
            }

            //upload and return imageurl
            String imgtag = "<img src=\""+imageurl+"\" />";
            table.after(imgtag);
            table.remove();
        }

        //image
        Elements images = doc.getElementsByTag("img");
        for (int i = 0; i < images.size() ; i++) {
            Element img = images.get(i);
            String oldsrc = img.attr("src");
            String subfix = oldsrc.substring(oldsrc.lastIndexOf("/")+1);
            String key = "";
            if(subfix.indexOf(".")>0){
                key = subfix;
            }else{
                key = subfix+".png";
            }
            String newsrc = fu.filefetch(oldsrc,key);
            img.attr("src", newsrc);
        }

        //deal with a super link including  gif png jpg jpegss
        Elements atags = doc.getElementsByTag("a");
        for (int i = 0; i < atags.size() ; i++) {
            Element atag = atags.get(i);
            String oldsrc = atag.attr("href");
            String subfix = oldsrc.substring(oldsrc.lastIndexOf("/") + 1);
            String key = "";
            if(subfix.indexOf(".gif")>0 || subfix.indexOf(".png")>0 || subfix.indexOf(".jpeg")>0 || subfix.indexOf(".jpg")>0){
                key = UUID.randomUUID().toString()+"_"+subfix;
                String newsrc = fu.filefetch(oldsrc,key);
                if(newsrc.equals("")){
                    newsrc = errorsrc;
                }
                atag.attr("href",newsrc+imgepraras);
            }
        }

        //清理
        String cleanStr = Jsoup.clean(doc.html(), baseUri, whitelist, outputSettings);
        System.out.println(cleanStr + "\n");

        HTML html = new HTML();
        String result = html.convert(cleanStr);
        System.out.println(result + "\n");
    }
}
