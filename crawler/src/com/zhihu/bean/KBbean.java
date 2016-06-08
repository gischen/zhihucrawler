package com.zhihu.bean; /**
 * Created by Administrator on 2015/8/19.
 */

/**
 * 封装KB的内容的bean
 */
public class KBbean {

    private String type = ""; //代表是什么类型，如howto,error,bug等
    private String title = "";
    private String detail = "";
    private String content = "";
    private String tag = ""; //代表话题  如howto bug
    private String source = "";//文章来源

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
