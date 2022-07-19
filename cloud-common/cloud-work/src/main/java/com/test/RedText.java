package com.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liulei
 */
public class RedText {
    public static void main(String[] args) throws Exception {

        Set<String> set = new HashSet();

        String filePath = "C:\\Users\\liulei\\Desktop\\imgss.txt";
        FileInputStream fin = new FileInputStream(filePath);
        InputStreamReader reader = new InputStreamReader(fin);
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            set.add(strTmp.trim());
        }
        buffReader.close();

        for(String str : set){
            System.out.print(str + " ");
        }

    }



}
