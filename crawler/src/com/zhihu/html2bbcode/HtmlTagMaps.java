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
//        htmlMap.put("<pre(.*?)>(.*?)</pre>","[code]$2[/code]");
//        htmlMap.put("<pre class=\"python\">","[code]");

//        htmlMap.put("<br /><br />","");
        htmlMap.put("<a(.*?)href=\"(.*?)\"(.*?)>(.*?)</a>","[url=$2]$4[/url]");
//        htmlMap.put("<a>","");//新浪
//        htmlMap.put("</a>","");//新浪
        htmlMap.put("<a></a>","");

        htmlMap.put("<img(.*?)src=\"(.*?)\"(.*?)>","[img]$2[/img]");

        htmlMap.put("<br(.*?)>", "\n");

        //blockquote

        htmlMap.put("<blockquote>", "\\[quote\\]");
        htmlMap.put("</blockquote>", "\\[/quote\\]");
        htmlMap.put("\\[quote\\] \n", "\\[quote\\]");
        htmlMap.put("\\[code\\] \n", "\\[code\\]");

        //code
//        htmlMap.put("<code>", "[code]");
//        htmlMap.put("</code>", "[/code]");
        htmlMap.put("<figure>", "[figure]");
        htmlMap.put("</figure>", "[/figure]");

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
        htmlMap.put("<h1(.*?)>(.*?)</h1>", "\n[size=30][b]$2[/b][/size]\n");
        htmlMap.put("<h2(.*?)>(.*?)</h2>", "\n[size=25][b]$2[/b][/size]\n");
        htmlMap.put("<h3(.*?)>(.*?)</h3>", "\n[size=20][b]$2[/b][/size]\n");
        htmlMap.put("<h4(.*?)>(.*?)</h4>", "\n[size=15][b]$2[/b][/size]\n");
        htmlMap.put("<h5(.*?)>(.*?)</h5>", "\n[size=10][b]$2[/b][/size]\n");
        htmlMap.put("<h6(.*?)>(.*?)</h6>", "\n[size=5][b]$2[/b][/size]\n");

        // italic
        htmlMap.put("<i>(.*?)</i>", "\\[i\\]$1\\[/i\\]");
        htmlMap.put("<u>(.*?)</u>", "\\[u\\]$1\\[/u\\]");

        //li
        //htmlMap.put("<li(.*?)>(.*?)</li>","[*]$2[/*]");
        htmlMap.put("<li>","[*]");
        htmlMap.put("</li>","[/*]");

        //ol
        htmlMap.put("<ol>", "[list=1]");
        htmlMap.put("</ol>", "[/list]");

        //p
        htmlMap.put("<p>(.*?)</p>","$1");


        //pre
//        htmlMap.put("<pre>", "");
//        htmlMap.put("<pre>", "");
//        htmlMap.put("<br /></pre>", "");
//        htmlMap.put("</pre>", "");

        //下角标
        htmlMap.put("<sub>", "");
        htmlMap.put("</sub>", "");

        //上角标
        htmlMap.put("<sup>", "");
        htmlMap.put("</sup>", "");

        //strong
        htmlMap.put("<strong>(.*?)</strong>", "[b]$1[/b]");
        htmlMap.put("<b>(.*?)</b>", "\\[b\\]$1\\[/b\\]");

        //span
       // htmlMap.put("<span(.*?)>(.*?)</span>", "\\[b\\]$2\\[/b\\]");

        //ul
        htmlMap.put("<ul(.*?)>", "[list]");
        htmlMap.put("</ul>", "[/list]");

        htmlMap.put("&quot;","\"");
        htmlMap.put("&gt;",">");
        htmlMap.put("&lt;","<");
        htmlMap.put("&nbsp;"," ");
        htmlMap.put("&middot;","    ·");
        htmlMap.put("&Oslash;","    >>>");
        htmlMap.put("&sup2;","    >>>");
        htmlMap.put("&sect; ","    >>>");
        htmlMap.put("&micro;","µ");
        htmlMap.put("&amp;","&");
        htmlMap.put("&deg;","°");
        htmlMap.put("&times;","×");
        htmlMap.put("&uuml;","    >>>");
        htmlMap.put("&agrave;","→");
        htmlMap.put("&plusmn;","±");
//        htmlMap.put("<table><tbody><tr><td>(.*?)</td><td>(.*?)</td></tr></tbody></table>","[code]$2[/code]");
//        htmlMap.put("<tr(.*?)>","");
//        htmlMap.put("<td(.*?)>","[code]");
//        htmlMap.put("</tr>","");
//        htmlMap.put("</td>","[/code]");
//        htmlMap.put("<table>","");
//        htmlMap.put("<tbody>","");
//        htmlMap.put("</table>","");
//        htmlMap.put("</tbody>","");


        htmlMap.put("<textarea(.*?)>(.*?)</textarea>","[code]$2[/code]");



        // videos
        htmlMap.put("<video(.*?)src=\"(.*?)\"(.*?)>", "[video]$2[/video\\]");
//        htmlMap.put(" \\n\\n","\n");
        return htmlMap;
    }
}
