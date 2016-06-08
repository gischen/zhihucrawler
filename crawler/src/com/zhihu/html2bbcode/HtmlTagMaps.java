package com.zhihu.html2bbcode;

import java.util.HashMap;
import java.util.Map;

public class HtmlTagMaps
{
    /**
     * blog 2 html
     * @return
     */
    public static Map<String, String> getHTMLMap()
    {
        Map<String, String> htmlMap = new HashMap<String, String>();

        // a
        htmlMap.put("<a href=\"(.+?)\" rel=\"nofollow\"></a>", "[url]$1[/url]");
        htmlMap.put("<a href=\"(.+?)\" rel=\"nofollow\">(.+?)</a>", "[url=$1]$2[/url]");
        htmlMap.put("<a rel=\"nofollow\">(.+?)</a>","");
        htmlMap.put("<a href=\"(.+?)\" title=\"(.+?)\">(.*?)</a>", "[url=$1]$3[/url]");
        htmlMap.put("<a href=\"(.+?)\">(.*)", "[url=$1]$2[/url]");
        htmlMap.put("<a href=\"(.+?)\">(.*?)</a>", "[url=$1]$2[/url]");
        htmlMap.put("<a>","");//新浪
        htmlMap.put("</a>","");//新浪
        htmlMap.put("<a></a>","");

	    //br
        htmlMap.put("<br />", "");
        htmlMap.put("<br>", "");

        //blockquote
        htmlMap.put("<blockquote>", "\\[quote\\]");
        htmlMap.put("</blockquote>", "\\[/quote\\]");

        //code
        htmlMap.put("<code>", "[code]");
        htmlMap.put("</code>", "[/code]");

        //dl dt dd
        htmlMap.put("<dl>", "[list]");
        htmlMap.put("</dl>", "[/list]");
        htmlMap.put("<dt>[\\s\\S][\\d\\D](.+?)[\\s\\S][\\d\\D]</dt>", "$1");
        htmlMap.put("<dd>[\\s\\S][\\d\\D](.+?)[\\s\\S][\\d\\D]</dd>", "    $1");

        // div
        htmlMap.put("<div>", "");
        htmlMap.put("</div>", "");

        //em
        htmlMap.put("<em>(.+?)</em>", "\\[i\\]$1\\[/i\\]");

        // hr
        htmlMap.put("<hr />", "[hr]");
        htmlMap.put("<hr>", "[hr]");

        // h title
        htmlMap.put("<h1>(.*)</h1>", "[size=30]$1[/size]");
        htmlMap.put("<h2>(.*)</h2>", "[size=25]$1[/size]");
        htmlMap.put("<h3>(.*)</h3>", "[size=20]$1[/size]");
        htmlMap.put("<h4>(.*)</h4>", "[size=15]$1[/size]");
        htmlMap.put("<h5>(.*)</h5>", "[size=10]$1[/size]");
        htmlMap.put("<h6>(.*)</h6>", "[size=5]$1[/size]");

        // italic
        htmlMap.put("<i>(.+?)</i>", "\\[i\\]$1\\[/i\\]");

        //li
        htmlMap.put("<li>(.+?)</li>","[*]$1[/*]");

        //images
        htmlMap.put("<img src='(.+?)'*>", "\\[img\\]$1\\[/img\\]");
        htmlMap.put("<img alt=\".*\" src=\"(.+?)\".*>", "[img]$1[/img]");
        htmlMap.put("<img src=\"(.+?)\".*>", "[img]$1[/img]");
        htmlMap.put("<img src=\"\" alt=\"\">","");


        //ol
        htmlMap.put("<ol>", "[list=1]");
        htmlMap.put("</ol>", "[/list]");

        //p
        htmlMap.put("<p>(.*?)</p>","$1");

        //pre
        htmlMap.put("<pre>", "");
        htmlMap.put("</pre>", "");

        //strong
        htmlMap.put("<strong>", "[b]");
        htmlMap.put("</strong>", "[/b]");
        htmlMap.put("<b>(.+?)</b>", "\\[b\\]$1\\[/b\\]");

        //span
        htmlMap.put("<span (.+?)>(.+?)</span>", "\\[b\\]$2\\[/b\\]");

        //ul
        htmlMap.put("<ul>", "[list=1]");
        htmlMap.put("</ul>", "[/list]");

        // underline
        htmlMap.put("<u>(.+?)</u>", "\\[u\\]$1\\[/u\\]");

        htmlMap.put("&quot;","\"");
        htmlMap.put("&gt;",">");
        htmlMap.put("&lt;","<");
        htmlMap.put("&nbsp;"," ");

        // videos
        htmlMap.put("<object width='(.+?)' height='(.+?)'><param name='(.+?)' value='http://www.youtube.com/v/(.+?)'></param><embed src='http://www.youtube.com/v/(.+?)' type='(.+?)' width='(.+?)' height='(.+?)'></embed></object>", "\\[youtube\\]$4\\[/youtube\\]");
        htmlMap.put("<object width=\"(.+?)\" height=\"(.+?)\"><param name=\"(.+?)\" value=\"http://www.youtube.com/v/(.+?)\"></param><embed src=\"http://www.youtube.com/v/(.+?)\" type=\"(.+?)\" width=\"(.+?)\" height=\"(.+?)\"></embed></object>", "\\[youtube\\]$4\\[/youtube\\]");
        htmlMap.put("<video src='(.+?)' />", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src=\"(.+?)\" />", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src='(.+?)'>", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src=\"(.+?)\">", "\\[video\\]$1\\[/video\\]");

        return htmlMap;
    }

    /**
     * word 2 html
     * @return
     */
    public static Map<String,String> getWordHtmlMap(){
        Map<String, String> htmlMap = new HashMap<String, String>();

        // a
        htmlMap.put("<a href=\"(.+?)\" rel=\"nofollow\"></a>", "[url]$1[/url]");
        htmlMap.put("<a href=\"(.+?)\" rel=\"nofollow\">(.+?)</a>", "[url=$1]$2[/url]");
        htmlMap.put("<a rel=\"nofollow\">(.+?)</a>","");
        htmlMap.put("<a href=\"(.+?)\" title=\"(.+?)\">(.*?)</a>", "[url=$1]$3[/url]");
        htmlMap.put("<a href=\"(.+?)\">(.*)", "[url=$1]$2[/url]");

        //word目录中的无用锚点
        //htmlMap.put("<a href=\"(((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?/#_Toc\\d*)\">(.*?)</a>", "$2");
        //htmlMap.put("<a href=\"(((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?/#_Toc\\d*)\">", "");

        htmlMap.put("<a href=\"(.+?)\">(.*?)</a>", "[url=$1]$2[/url]");

        htmlMap.put("<a>","");//新浪
        htmlMap.put("</a>","");//新浪
        htmlMap.put("<a></a>","");

        //br
        htmlMap.put("<br />", "");
        htmlMap.put("<br>", "");

        //blockquote
        htmlMap.put("<blockquote>", "\\[quote\\]");
        htmlMap.put("</blockquote>", "\\[/quote\\]");

        //code
        htmlMap.put("<code>", "[code]");
        htmlMap.put("</code>", "[/code]");

        //dl dt dd
        htmlMap.put("<dl>", "[list]");
        htmlMap.put("</dl>", "[/list]");
        htmlMap.put("<dt>[\\s\\S][\\d\\D](.+?)[\\s\\S][\\d\\D]</dt>", "$1");
        htmlMap.put("<dd>[\\s\\S][\\d\\D](.+?)[\\s\\S][\\d\\D]</dd>", "    $1");

        // div
        htmlMap.put("<div>", "");
        htmlMap.put("</div>", "");

        //em
        htmlMap.put("<em>(.+?)</em>", "\\[i\\]$1\\[/i\\]");

        // hr
        htmlMap.put("<hr />", "[hr]");
        htmlMap.put("<hr>", "[hr]");

        // h title
        htmlMap.put("<h1>(.*)</h1>", "[size=36]$1[/size]");
        htmlMap.put("<h2>(.*)</h2>", "[size=23]$1[/size]");
        htmlMap.put("<h3>(.*)</h3>", "[size=20]$1[/size]");
        htmlMap.put("<h4>(.*)</h4>", "[size=15]$1[/size]");
        htmlMap.put("<h5>(.*)</h5>", "[size=10]$1[/size]");
        htmlMap.put("<h6>(.*)</h6>", "[size=5]$1[/size]");

        // italic
        htmlMap.put("<i>(.+?)</i>", "\\[i\\]$1\\[/i\\]");

        //li
        htmlMap.put("<li>(.+?)</li>","[*]$1[/*]");

        //images
        htmlMap.put("<img src='(.+?)'*>", "\\[img\\]$1\\[/img\\]");
        htmlMap.put("<img alt=\".*\" src=\"(.+?)\".*>", "[img]$1[/img]");
        htmlMap.put("<img src=\"(.+?)\".*>", "[img]$1[/img]");
        htmlMap.put("<img src=\"\" alt=\"\">","");


        //ol
        htmlMap.put("<ol>", "[list=1]");
        htmlMap.put("</ol>", "[/list]");

        //p
        htmlMap.put("<p>(.*?)</p>","$1\n");//p new line

        //pre
        htmlMap.put("<pre>", "");
        htmlMap.put("</pre>", "");

        //strong
        htmlMap.put("<strong>", "[b]");
        htmlMap.put("</strong>", "[/b]");
        htmlMap.put("<b>(.*?)</b>", "[b]$1[/b]");

        //span
        htmlMap.put("<span (.+?)>(.+?)</span>", "\\[b\\]$2\\[/b\\]");

        //ul
        htmlMap.put("<ul>", "[list=1]");
        htmlMap.put("</ul>", "[/list]");

        // underline
        htmlMap.put("<u>(.+?)</u>", "\\[u\\]$1\\[/u\\]");

        htmlMap.put("&quot;","\"");
        htmlMap.put("&gt;",">");
        htmlMap.put("&lt;","<");
        htmlMap.put("&nbsp;"," ");
        htmlMap.put("&middot;","·");

        // videos
        htmlMap.put("<object width='(.+?)' height='(.+?)'><param name='(.+?)' value='http://www.youtube.com/v/(.+?)'></param><embed src='http://www.youtube.com/v/(.+?)' type='(.+?)' width='(.+?)' height='(.+?)'></embed></object>", "\\[youtube\\]$4\\[/youtube\\]");
        htmlMap.put("<object width=\"(.+?)\" height=\"(.+?)\"><param name=\"(.+?)\" value=\"http://www.youtube.com/v/(.+?)\"></param><embed src=\"http://www.youtube.com/v/(.+?)\" type=\"(.+?)\" width=\"(.+?)\" height=\"(.+?)\"></embed></object>", "\\[youtube\\]$4\\[/youtube\\]");
        htmlMap.put("<video src='(.+?)' />", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src=\"(.+?)\" />", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src='(.+?)'>", "\\[video\\]$1\\[/video\\]");
        htmlMap.put("<video src=\"(.+?)\">", "\\[video\\]$1\\[/video\\]");

        return htmlMap;
    }
}
