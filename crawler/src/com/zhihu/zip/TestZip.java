package com.zhihu.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class TestZip {
    public static void main(String[] args) {
        List<File> srcfile=new ArrayList<File>();
        srcfile.add(new File("e:\\workspace\\java-kepler\\2015-07\\2015��07��01����2015��07��15������������֧�����ݷ�������.docx"));
        srcfile.add(new File("e:\\workspace\\java-kepler\\2015-07\\2015��07��01����2015��07��15������������֧�����ݷ�������.docx"));
        srcfile.add(new File("e:\\workspace\\java-kepler\\2015-07\\2015��07��01����2015��07��15�ռ���֧�����ݷ�������.docx"));
        srcfile.add(new File("e:\\workspace\\java-kepler\\2015-07\\2015��07��01����2015��07��15�ջ���������֧�����ݷ�������.docx"));

        File zipfile = new File("e:\\2015��7��.zip");
        ZipUtil.zipFiles(srcfile, zipfile);
    }
}
