package com.zhihu.html2bbcode;

import com.qiniu.storage.UploadManager;
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
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2015/9/24.
 */
public class HTML {

    private static HTML myHandler = new HTML();
    private static FileUpload fu = new FileUpload();

    public static HTML getHandler()
    {
        return myHandler;
    }

    /**
     * html 转 bbcode
     * @param input
     * @return
     */
    public static String convert(String input)
    {
        String bbcode = input;

        for (Map.Entry entry: HtmlTagMaps.getHTMLMap().entrySet())
        {
            bbcode = bbcode.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return bbcode;
    }

    /**
     * word 2 bbcode
     * @param input
     * @return
     */
    public static String covertWord2bbcode(String input){

        String bbcode = input;

        for (Map.Entry entry: HtmlTagMaps.getWordHtmlMap().entrySet())
        {
            bbcode = bbcode.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return bbcode;
    }

    public void check(String input)
    {
        int check = 0;

        for (int i = 0; i < input.length(); i++)
        {
            if (input.charAt(i) == '<')
                check++;
            else if (input.charAt(i) == '>')
                check--;
        }

        if (check != 0)
        {
            System.out.print("==========There are some errors in your code, please check it!");
            return;
        }

        String links[] = URLChecks.findHTMLlinks(input);

        for (int i = 0; i < links.length; i++)
        {
            if (!URLChecks.exists(links[i]))
            {
                System.out.print("=========One or more links may be invalid.");
                return;
            }
        }

        System.out.print("============Everything's ok!");
    }

    /**
     *
     * @param contentStr 要转换的字符串
     * @param baseUri  网站基础网址
     * @param temppath 图片临时路径
     * @param errsrc  错误图片的URL
     * @return
     */
    public static String html2bbcode(String contentStr,String baseUri,String temppath,String errsrc) {

        String basictags[] = new String[]{"a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup", "u", "ul"};
        String additionaltags[] = {"h1","h2","h3","h4","h5","h6","img"};
        String tabletags[] = {"table","thead","tbody","tfoot","tr","td"};

        String uri = baseUri;
        String filePath = temppath;

        Whitelist whitelist = new Whitelist()
                .addTags(basictags).addTags(additionaltags).addTags(tabletags)
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

        UploadManager uploadManager = new UploadManager();

        String errorsrc = errsrc;

        //选择出table，然后替换成img标签
        Document doc = Jsoup.parse(contentStr);
        Elements tables = doc.getElementsByTag("table");
        for (int i = 0; i < tables.size() ; i++) {
            Element table = tables.get(i);

            final HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
            imageGenerator.loadHtml(table.outerHtml());

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

        //deal with image
        Elements images = doc.getElementsByTag("img");
        for (int i = 0; i < images.size() ; i++) {
            Element img = images.get(i);
            String oldsrc = img.attr("src");
            String key = "";
            String subfix = "";

            if(oldsrc.contains("simg.sinajs.cn")){ //处理新浪博客的图片链接
                oldsrc = img.attr("real_src");
                int start = oldsrc.lastIndexOf("/");
                int end = oldsrc.lastIndexOf("&");
                if(start>0 && end >0 && end>start) {
                    subfix = oldsrc.substring(start+1, end);
                    key = subfix;
                }else{
                    key = UUID.randomUUID().toString();
                }
            }else{
                int start1 = oldsrc.lastIndexOf("/");
                int startdot = oldsrc.lastIndexOf(".");
                if(start1 >0 && startdot >0 && startdot>start1) {
                    subfix = oldsrc.substring(start1 + 1);
                    key = subfix;
                }else if(start1 > 0){
                    subfix = oldsrc.substring(start1 + 1)+UUID.randomUUID().toString();
                    key = subfix;
                }
            }

            String newsrc = fu.filefetch(oldsrc,key);
            System.out.println("====img====图片上传返回的URL:"+newsrc);

            if(newsrc.equals("")){
                newsrc = errorsrc;
            }
            img.attr("src", newsrc);
        }

        //deal with a super link including  gif png jpg jpeg bmp
        Elements atags = doc.getElementsByTag("a");
        for (int i = 0; i < atags.size() ; i++) {
            Element atag = atags.get(i);
            String oldsrc = atag.attr("href");
            String subfix = oldsrc.substring(oldsrc.lastIndexOf("/") + 1);
            String key = "";
            if(!oldsrc.startsWith("http://photo.blog.sina.com.cn/showpic.html")) {//处理新浪连接
                if (subfix.indexOf(".gif") > 0 || subfix.indexOf(".png") > 0 || subfix.indexOf(".jpeg") > 0 || subfix.indexOf(".jpg") > 0 || subfix.indexOf(".bmp") > 0) {
                    key = subfix;
                    String newsrc = fu.filefetch(oldsrc, key);
                    System.out.println("===a===图片上传返回的URL:"+newsrc);
                    if (newsrc.equals("")) {
                        newsrc = errorsrc;
                    }
                    atag.attr("href", newsrc);
                }
            }else {
                atag.removeAttr("href");
            }
        }

        String cleanStr = Jsoup.clean(doc.html(), uri, whitelist, outputSettings);

        String result = convert(cleanStr);

        return result;
    }

    /**
     * word转html，然后转bbcode
     * @param contentStr
     * @param baseUri
     * @param temppath
     * @param errsrc
     * @return
     */
    public static String word2html2bbcode(String contentStr,String baseUri,String temppath,String errsrc){

        String result = "";

        String basictags[] = new String[]{"a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "strike", "strong", "sub", "sup", "u", "ul"};
        String additionaltags[] = {"h1","h2","h3","h4","h5","h6","img"};
        String tabletags[] = {"table","thead","tbody","tfoot","tr","td"};

        String uri = baseUri;
        String filePath = temppath;
        String errorsrc = errsrc;

        Whitelist whitelist = new Whitelist()
                .addTags(basictags).addTags(additionaltags).addTags(tabletags)
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

        String cleanStr = Jsoup.clean(contentStr, uri, whitelist, outputSettings);
        result = covertWord2bbcode(cleanStr);

        return result;
    }



}
