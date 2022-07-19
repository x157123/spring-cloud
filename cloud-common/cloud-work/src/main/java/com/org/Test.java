package com.org;

public class Test {
    public static void main(String args[]) {
        String  s="武滩村三组";

        s=s.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）|0[0-9]*$|0[0-9]*号$|[0-9]*社$|村.*?组$", "");

        System.out.println(s);
        //我们都是中国人
    }
}
