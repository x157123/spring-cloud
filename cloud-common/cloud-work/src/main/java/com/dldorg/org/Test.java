package com.dldorg.org;

import java.util.regex.Pattern;

public class Test {
    public static void main(String args[]) {

        String s = "02网格";
        String ss = "网格2";
        String strsss = ".1.1.23.";
        String st = strsss.substring(0, strsss.indexOf(".", strsss.indexOf(".") + 1) + 1);

        System.out.println(st);

        String str  = s.replaceAll("[0-9]*网格$", "");

        System.out.println(s);

        if(str.length()<=0){
            String code = "100元/年";
            //匹配非数字字符，然后全部替换为空字符，剩下的自然只有数字啦
            String sss = Pattern.compile("[^0-9]").matcher(s).replaceAll("");
            //打印结果 100
            String news = s.replace(sss,"");
            String ssssss = news+Long.parseLong(sss);
        }

        //我们都是中国人
    }
}
