package com.zhihu.html2bbcode;

import java.util.Map;

/**
 * Created by Administrator on 2015/9/24.
 */
public class BBCode
{
    private static BBCode myHandler = new BBCode();

    public static BBCode getHandler()
    {
        return myHandler;
    }

    /**
     * bbcode 转 html
     * @param input
     * @return
     */
    public String convert(String input)
    {
        String html = input;

        for (Map.Entry entry: BBcodeTagMaps.getBBcodeMap().entrySet())
        {
            html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
        }

        return html;

    }

    /**
     * BBcode data format check
     * @param input
     */
    public void check(String input)
    {
        int check = 0;

        for (int i = 0; i < input.length(); i++)
        {
            if (input.charAt(i) == '[')
                check++;
            else if (input.charAt(i) == ']')
                check--;
        }

        if (check != 0)
        {
            System.out.print("=========BBcode data format exist error!!!!!!数据格式存在问题!!!!!!!!!!!");
            return;
        }

        String links[] = URLChecks.findBBCodelinks(input);

        for (int i = 0; i < links.length; i++)
        {
            if (!URLChecks.exists(links[i]))
            {
                System.out.print("===========BBcode One or more links may be invalid!!!!!!!!!!!!!!!!!!!!BBcode中的链接存在问题!!!!!!!!");
                return;
            }
        }
        System.out.print("===========Everything's ok!");
    }

}
