package com.cloud.auto.code.util;

import org.springframework.util.StringUtils;

/**
 * 文本工具类
 * @author liulei
 */
public class StringUtil {

    /**
     * 数据库字段转java驼峰命名
     * @param dbName
     * @return
     */
    public static String dbToClassName(String dbName){
        StringBuffer sb = new StringBuffer();
        String[] sirs = dbName.split("_");
        for(String str : sirs){
            sb.append(toUpperCaseFirstOne(str));
        }
        return sb.toString();
    }


    /**
     * 首字母转小写
     *
     * @param str
     * @return
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        if (str.length()<=0 || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }


    public static String getString(String str){
        if(StringUtils.hasLength(str)){
            return str;
        }
        return "";
    }
}
