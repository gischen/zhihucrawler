package com.zhihu.doc.word;

import com.qiniu.storage.UploadManager;
import com.zhihu.bean.Blogbean;
import com.zhihu.html2bbcode.HTML;
import com.zhihu.qiniu.FileUpload;
import com.zhihu.util.FileNameExt;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.extractor.ParsingEmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Word2Html {

    private static Logger logger = Logger.getLogger(Word2Html.class);

    private Parser parser = new AutoDetectParser();
    private Detector detector = ((AutoDetectParser) parser).getDetector();
    private TikaConfig config = TikaConfig.getDefaultConfig();

    private static FileUpload fu = new FileUpload();
    private HashMap<String,String> imgPathMap = new HashMap<String,String>();
    UploadManager uploadManager = new UploadManager();

    private Properties properties = new Properties();
    String temppath = "";
    String errorsrc = "";
    String baseurl = "http://7xospm.com1.z0.glb.clouddn.com/";

    public Word2Html() {

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


    /**
     * @param file word文件
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws TikaException
     */
    public Blogbean parseToHTML(File file) throws IOException, SAXException, TikaException {

        String result = "";

        Metadata metadata = new Metadata();
        ContentHandler contentHandler = new ToHTMLContentHandler();
        AutoDetectParser parser = new AutoDetectParser();


        ParseContext parseContext = new ParseContext();
        parseContext.set(Parser.class, parser);
        EmbeddedDocumentExtractor embeddedDocumentExtractor = new MyEmbeddedDocumentExtractor(Paths.get(temppath), parseContext);
        parseContext.set(EmbeddedDocumentExtractor.class, embeddedDocumentExtractor);

        InputStream inputStream = new FileInputStream(file);
        parser.parse(inputStream, contentHandler, metadata,parseContext);
        result = contentHandler.toString();

        //logger.info(result);

        //对result处理，替换html中<img>便签中的src的值
        Document doc = Jsoup.parse(result);
        Elements images = doc.getElementsByTag("img");
        for (int i = 0; i < images.size() ; i++) {
            Element img = images.get(i);
            String oldSrc = img.attr("src");
            String preStr = ":";
            int index = oldSrc.indexOf(preStr);

            String name = oldSrc.substring(index+1);

            String newSrc = imgPathMap.get(name);
            if(newSrc != null) {
                img.attr("src", newSrc);
            }
        }

        //去除掉无用的锚点
        Elements hrefs = doc.getElementsByTag("a");
        for (int i = 0; i < hrefs.size() ; i++) {
            Element a = hrefs.get(i);
            String oldSrc = a.attr("href");
            String preStr = "#_Toc";
            if(oldSrc.contains(preStr)){
                a.remove();
            }
        }

        //deal with table label
        Elements tables = doc.getElementsByTag("table");
        for (int i = 0; i < tables.size() ; i++) {
            Element table = tables.get(i);

            final HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
            imageGenerator.loadHtml(table.outerHtml());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = DateFormat.format(calendar.getTime());

            String fileName = dateStr+"_"+UUID.randomUUID().toString()+"_table"+".png";
            imageGenerator.saveAsImage(temppath + fileName);

            String imageurl = "";
            try {
                File filetable = new File(temppath+fileName);
                InputStream in = new FileInputStream(filetable);
                byte b[]=new byte[(int)filetable.length()];
                in.read(b);
                in.close();
                imageurl = fu.fileUpload(uploadManager, b, fileName, null, "image/png");
                logger.info("=====table url:"+imageurl);

            }catch (Exception e){
                e.printStackTrace();
            }

            //upload and return imageurl
            String imgtag = "<img src=\""+imageurl+"\" />";
            table.after(imgtag);
            table.remove();
        }

        result = HTML.word2html2bbcode(doc.html(),baseurl,temppath,errorsrc);

        //logger.info(result);

        Blogbean blogbean = new Blogbean();
        blogbean.setTitle(FileNameExt.getFileNameNoEx(file.getName()));
        blogbean.setContent(result);
        blogbean.setSource("");
        blogbean.setAuthor("ArcGIS知乎");

        return blogbean;
    }

    /**
     * extract image from word document
     */
    private class MyEmbeddedDocumentExtractor extends ParsingEmbeddedDocumentExtractor {
        private final Path outputDir;
        private int fileCount = 0;

        private MyEmbeddedDocumentExtractor(Path outputDir, ParseContext context) {
            super(context);
            this.outputDir = outputDir;
        }

        @Override
        public boolean shouldParseEmbedded(Metadata metadata) {
            return true;
        }

        @Override
        public void parseEmbedded(InputStream stream, ContentHandler handler, Metadata metadata, boolean outputHtml)
                throws SAXException, IOException {

            //try to get the name of the embedded file from the metadata
            String name = metadata.get(Metadata.RESOURCE_NAME_KEY);

            if (name == null) {
                name = "file_" + fileCount++;
            } else {
                //make sure to select only the file name (not any directory paths
                //that might be included in the name) and make sure
                //to normalize the name
                name = FilenameUtils.normalize(FilenameUtils.getName(name));
            }

            //now try to figure out the right extension for the embedded file
            MediaType contentType = detector.detect(stream, metadata);

            if (name.indexOf('.') == -1 && contentType != null) {
                try {
                    name += config.getMimeRepository().forName(
                            contentType.toString()).getExtension();
                } catch (MimeTypeException e) {
                    e.printStackTrace();
                }
            }
            //should add check to make sure that you aren't overwriting a file
            Path outputFile = outputDir.resolve(name);
            //do a better job than this of checking
            java.nio.file.Files.createDirectories(outputFile.getParent());
            java.nio.file.Files.copy(stream, outputFile, StandardCopyOption.REPLACE_EXISTING);

            //上传图片，返回Url，并建立一个映射表
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = DateFormat.format(calendar.getTime());

            String key = dateStr+"_"+UUID.randomUUID().toString()+"_"+name;
            String qiniuurl = "";
            try {
                File file = new File(outputFile.toString());
                InputStream in = new FileInputStream(file);
                byte b[]=new byte[(int)file.length()];
                in.read(b);
                in.close();
                qiniuurl = fu.fileUpload(uploadManager, b, key, null, contentType.toString());
                imgPathMap.put(name,qiniuurl);
                logger.info("=====图片url："+qiniuurl);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
