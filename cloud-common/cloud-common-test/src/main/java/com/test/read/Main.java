package com.test.read;

import com.test.read.bo.DatabaseParam;
import com.test.read.utils.DbUtil;
import com.test.read.utils.ReadExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author liulei
 */
public class Main {
    public static void main(String[] args) {

        DbUtil dbUtil = new DbUtil();
        ArrayList<String> files = new ArrayList<String>();
        File file = new File("C:\\Users\\liulei\\Desktop\\信息字段清单\\推送大联动数据模板字段清单");
        File[] tempList = file.listFiles();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String[] names = tempList[i].getName().split("\\.")[0].split("--");
                if (names.length < 3) {
                    continue;
                }
                List<DatabaseParam> list = ReadExcel.readExcel(tempList[i].toString());
                boolean printSql = false;
                while (!printSql) {
                    Integer index = 0;
                    while (index <= 0) {
                        dbUtil.showDbList();
                        System.out.print(names[1] + "数据库：" + names[0] + " 表名：" + names[2] + "   请选择数据库序号 Enter 继续: ");
                        index = sc.nextInt();
                        index = dbUtil.getDb(index);
                    }
                    System.out.print("默认表名：" + names[2] + " 输入：1 Enter 继续 输入其他字符替换表名: ");
                    String tableName = sc.next();
                    if (tableName.trim().length() <= 0 || "1".equals(tableName)) {
                        tableName = names[2];
                    }
                    printSql = dbUtil.createTableSql(index, tableName, names[1] ,list);
                    if (!printSql) {
                        System.out.println("生成错误");
                    }
                }
            }
        }

    }
}
