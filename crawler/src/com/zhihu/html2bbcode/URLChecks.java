package com.zhihu.html2bbcode;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/9/24.
 */
public class URLChecks {

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] findHTMLlinks(String input) {
        String[] links;

        int count = 0;

        for (int i = 0; i < input.length() - 5; i++) {
            if (
                    input.substring(i, i + 5).compareTo("src='") == 0 ||
                    input.substring(i, i + 5).compareTo("src=\"") == 0 ||
                    input.substring(i, i + 5).compareTo("ref='") == 0 ||
                    input.substring(i, i + 5).compareTo("ref=\"") == 0 ||
                    input.substring(i, i + 5).compareTo("SRC='") == 0 ||
                    input.substring(i, i + 5).compareTo("SRC=\"") == 0 ||
                    input.substring(i, i + 5).compareTo("REF='") == 0 ||
                    input.substring(i, i + 5).compareTo("REF=\"") == 0
                    )
                count++;
        }

        links = new String[count];
        int position = 0;

        for (int i = 0; i < count; i++) {
            for (; position < input.length() - 5; position++) {
                if (
                        input.substring(position, position + 5).compareTo("src='") == 0 ||
                        input.substring(position, position + 5).compareTo("src=\"") == 0 ||
                        input.substring(position, position + 5).compareTo("ref='") == 0 ||
                        input.substring(position, position + 5).compareTo("ref=\"") == 0 ||
                        input.substring(position, position + 5).compareTo("SRC='") == 0 ||
                        input.substring(position, position + 5).compareTo("SRC=\"") == 0 ||
                        input.substring(position, position + 5).compareTo("REF='") == 0 ||
                        input.substring(position, position + 5).compareTo("REF=\"") == 0
                        ) {
                    position = position + 5;
                    int pos2 = position + 1;
                    while (input.charAt(pos2) != '\"' && input.charAt(pos2) != '\'')
                        pos2++;

                    links[i] = input.substring(position, pos2);
                    position = pos2;
                    break;
                }
            }
        }

        return links;
    }

    public static String[] findBBCodelinks(String input) {
        String[] links;

        int count = 0;

        for (int i = 0; i < input.length() - 5; i++) {
            if (
                    input.substring(i, i + 4).compareTo("url=") == 0 ||
                    input.substring(i, i + 4).compareTo("url]") == 0 ||
                    input.substring(i, i + 4).compareTo("img=") == 0 ||
                    input.substring(i, i + 4).compareTo("img]") == 0 ||
                    input.substring(i, i + 4).compareTo("deo]") == 0 ||
                    input.substring(i, i + 4).compareTo("URL=") == 0 ||
                    input.substring(i, i + 4).compareTo("URL]") == 0 ||
                    input.substring(i, i + 4).compareTo("IMG=") == 0 ||
                    input.substring(i, i + 4).compareTo("IMG]") == 0 ||
                    input.substring(i, i + 4).compareTo("DEO]") == 0
                    )
                count++;
        }

        links = new String[count];
        int position = 0;

        for (int i = 0; i < count; i++) {
            for (; position < input.length() - 5; position++) {
                if (
                        input.substring(position, position + 4).compareTo("url=") == 0 ||
                        input.substring(position, position + 4).compareTo("url]") == 0 ||
                        input.substring(position, position + 4).compareTo("img=") == 0 ||
                        input.substring(position, position + 4).compareTo("img]") == 0 ||
                        input.substring(position, position + 4).compareTo("deo]") == 0 ||
                        input.substring(position, position + 4).compareTo("URL=") == 0 ||
                        input.substring(position, position + 4).compareTo("URL]") == 0 ||
                        input.substring(position, position + 4).compareTo("IMG=") == 0 ||
                        input.substring(position, position + 4).compareTo("IMG]") == 0 ||
                        input.substring(position, position + 4).compareTo("DEO]") == 0
                        ) {
                    position = position + 4;
                    int pos2 = position + 1;
                    while (input.charAt(pos2) != ']' && input.charAt(pos2) != '[')
                        pos2++;

                    links[i] = input.substring(position, pos2);
                    position = pos2;
                    break;
                }
            }
        }
        return links;
    }
}
