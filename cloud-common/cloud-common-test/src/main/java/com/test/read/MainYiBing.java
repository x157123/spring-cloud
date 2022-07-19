package com.test.read;

import com.test.read.bo.DatabaseParam;
import com.test.read.utils.DbUtil;
import com.test.read.utils.ReadExcel;
import com.test.read.utils.ReadExcelNew;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author liulei
 */
public class MainYiBing {
    public static void main(String[] args) {

        DbUtil dbUtil = new DbUtil();
        ArrayList<String> files = new ArrayList<String>();
        File file = new File("C:\\Users\\liulei\\Desktop\\信息字段清单\\宜宾");
        File[] tempList = file.listFiles();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String names = tempList[i].getName();
                ReadExcelNew.readExcel(tempList[i].toString());
            }
        }

    }
}
