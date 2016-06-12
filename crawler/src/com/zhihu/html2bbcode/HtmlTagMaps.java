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

        htmlMap.put("<a(.*?)href=\"(.*?)\"(.*?)>(.*?)</a>","[url=$2]$4[/url]");
        htmlMap.put("<a>","");//新浪
        htmlMap.put("</a>","");//新浪
        htmlMap.put("<a></a>","");

        htmlMap.put("<img(.*?)src=\"(.*?)\"(.*?)>","[img]$2[/img]");
        htmlMap.put("<br(.*?)>", "");

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
        htmlMap.put("<hr(.*?)>", "[hr]");

        // h title
        htmlMap.put("<h1(.*?)>((.*?))</h1>", "[size=30]$2[/size]");
        htmlMap.put("<h2(.*?)>(.*?)</h2>", "[size=25]$2[/size]");
        htmlMap.put("<h3(.*?)>(.*?)</h3>", "[size=20]$2[/size]");
        htmlMap.put("<h4(.*?)>(.*?)</h4>", "[size=15]$2[/size]");
        htmlMap.put("<h5(.*?)>(.*?)</h5>", "[size=10]$2[/size]");
        htmlMap.put("<h6(.*?)>(.*?)</h6>", "[size=5]$2[/size]");

        // italic
        htmlMap.put("<i>(.*?)</i>", "\\[i\\]$1\\[/i\\]");
        htmlMap.put("<u>(.*?)</u>", "\\[u\\]$1\\[/u\\]");

        //li
        htmlMap.put("<li(.*?)>(.*?)</li>","[*]$2[/*]");

        //ol
        htmlMap.put("<ol>", "[list=1]");
        htmlMap.put("</ol>", "[/list]");

        //p
        htmlMap.put("<p>(.*?)</p>","$1");

        //pre
        htmlMap.put("<pre>", "");
        htmlMap.put("</pre>", "");

        //strong
        htmlMap.put("<strong>(.*?)</strong>", "[b]$1[/b]");
        htmlMap.put("<b>(.*?)</b>", "\\[b\\]$1\\[/b\\]");

        //span
        htmlMap.put("<span(.*?)>(.*?)</span>", "\\[b\\]$2\\[/b\\]");

        //ul
        htmlMap.put("<ul(.*?)>", "[list]");
        htmlMap.put("</ul>", "[/list]");

        htmlMap.put("&quot;","\"");
        htmlMap.put("&gt;",">");
        htmlMap.put("&lt;","<");
        htmlMap.put("&nbsp;"," ");

        htmlMap.put("<tr(.*?)>","");
        htmlMap.put("<td(.*?)>","");

        htmlMap.put("<textarea(.*?)>(.*?)</textarea>","[code]$2[/code]");

        // videos
        htmlMap.put("<video(.*?)src=\"(.*?)\"(.*?)>", "[video]$2[/video\\]");

        return htmlMap;
    }
}
