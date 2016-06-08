package com.zhihu.html2bbcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/25.
 */
public class BBcodeTagMaps {

    public static Map<String, String> getBBcodeMap()
    {
        Map<String, String> bbMap = new HashMap<String, String>();

	    /* lowercase */

        bbMap.put("\n", "<br />");
        bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
        bbMap.put("\\[i\\](.+?)\\[/i\\]", "<i>$1</i>");
        bbMap.put("\\[u\\](.+?)\\[/u\\]", "<u>$1</u>");
        bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
        bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
        bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
        bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
        bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
        bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
        bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
        bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
        bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style=\"text-indent:$1px;line-height:$2%;\">$3</p>");
        bbMap.put("\\[center\\](.+?)\\[/center\\]", "<div align=\"center\">$1");
        bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align=\"$1\">$2");
        bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style=\"color:$1;\">$2</span>");
        bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style=\"font-size:$1;\">$2</span>");
        bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src=\"$1\" />");
        bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width=\"$1\" height=\"$2\" src=\"$3\" />");
        bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href=\"mailto:$1\">$1</a>");
        bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href=\"mailto:$1\">$2</a>");
        bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href=\"$1\">$1</a>");
        bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href=\"$1\">$2</a>");
        bbMap.put("\\[youtube\\](.+?)\\[/youtube\\]", "<object width=\"640\" height=\"380\"><param name=\"movie\" value=\"http://www.youtube.com/v/$1\"></param><embed src=\"http://www.youtube.com/v/$1\" type=\"application/x-shockwave-flash\" width=\"640\" height=\"380\"></embed></object>");
        bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src=\"$1\" />");


        /* UPPERCASE */

        bbMap.put("\\[B\\](.+?)\\[/B\\]", "<STRONG>$1</STRONG>");
        bbMap.put("\\[I\\](.+?)\\[/I\\]", "<I>$1</I>");
        bbMap.put("\\[U\\](.+?)\\[/U\\]", "<U>$1</U>");
        bbMap.put("\\[H1\\](.+?)\\[/H1\\]", "<H1>$1</H1>");
        bbMap.put("\\[H2\\](.+?)\\[/H2\\]", "<H2>$1</H2>");
        bbMap.put("\\[H3\\](.+?)\\[/H3\\]", "<H3>$1</H3>");
        bbMap.put("\\[H4\\](.+?)\\[/H4\\]", "<H4>$1</H4>");
        bbMap.put("\\[H5\\](.+?)\\[/H5\\]", "<H5>$1</H5>");
        bbMap.put("\\[H6\\](.+?)\\[/H6\\]", "<H6>$1</H6>");
        bbMap.put("\\[QUOTE\\](.+?)\\[/QUOTE\\]", "<BLOCKQUOTE>$1</BLOCKQUOTE>");
        bbMap.put("\\[P\\](.+?)\\[/P\\]", "<P>$1</P>");
        bbMap.put("\\[P=(.+?),(.+?)\\](.+?)\\[/P\\]", "<P STYLE=\"TEXT-INDENT:$1PX;LINE-HEIGHT:$2%;\">$3</P>");
        bbMap.put("\\[CENTER\\](.+?)\\[/CENTER\\]", "<DIV ALIGN=\"CENTER\">$1");
        bbMap.put("\\[ALIGN=(.+?)\\](.+?)\\[/ALIGN\\]", "<DIV ALIGN=\"$1\">$2");
        bbMap.put("\\[COLOR=(.+?)\\](.+?)\\[/COLOR\\]", "<SPAN STYLE=\"COLOR:$1;\">$2</SPAN>");
        bbMap.put("\\[SIZE=(.+?)\\](.+?)\\[/SIZE\\]", "<SPAN STYLE=\"FONT-SIZE:$1;\">$2</SPAN>");
        bbMap.put("\\[IMG\\](.+?)\\[/IMG\\]", "<IMG SRC=\"$1\" />");
        bbMap.put("\\[IMG=(.+?),(.+?)\\](.+?)\\[/IMG\\]", "<IMG WIDTH=\"$1\" HEIGHT=\"$2\" SRC=\"$3\" />");
        bbMap.put("\\[EMAIL\\](.+?)\\[/EMAIL\\]", "<A HREF=\"MAILTO:$1\">$1</A>");
        bbMap.put("\\[EMAIL=(.+?)\\](.+?)\\[/EMAIL\\]", "<A HREF=\"MAILTO:$1\">$2</A>");
        bbMap.put("\\[URL\\](.+?)\\[/URL\\]", "<A HREF=\"$1\">$1</A>");
        bbMap.put("\\[URL=(.+?)\\](.+?)\\[/URL\\]", "<A HREF=\"$1\">$2</A>");
        bbMap.put("\\[YOUTUBE\\](.+?)\\[/YOUTUBE\\]", "<OBJECT WIDTH=\"640\" HEIGHT=\"380\"><PARAM NAME=\"MOVIE\" VALUE=\"HTTP://WWW.YOUTUBE.COM/V/$1\"></PARAM><EMBED SRC=\"HTTP://WWW.YOUTUBE.COM/V/$1\" TYPE=\"APPLICATION/X-SHOCKWAVE-FLASH\" WIDTH=\"640\" HEIGHT=\"380\"></EMBED></OBJECT>");
        bbMap.put("\\[VIDEO\\](.+?)\\[/VIDEO\\]", "<VIDEO SRC=\"$1\" />");

        return bbMap;
    }

}
