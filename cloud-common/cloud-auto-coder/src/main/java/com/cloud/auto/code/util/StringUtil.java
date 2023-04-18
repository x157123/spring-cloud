package com.cloud.auto.code.util;

import com.cloud.common.core.utils.BeanUtil;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本工具类
 *
 * @author liulei
 */
public class StringUtil {

    private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 数据库字段转java驼峰命名
     *
     * @param dbName
     * @return
     */
    public static String dbToClassName(String dbName) {
        String[] words = dbName.split("_");
        StringBuilder stringBuilder = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            stringBuilder.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));
        }
        return stringBuilder.toString();
    }

    /**
     * 获取ClassName
     *
     * @return
     */

    public static String getClassName(String name) {
        return getClassName(name, null);
    }

    public static String getClassName(String name, List<String> prefix) {
        String className = name;
        if (prefix != null && prefix.size() > 0) {
            for (String pre : prefix) {
                int i = className.indexOf(pre);
                if (i == 0) {
                    className = className.substring(pre.length());
                }
            }
        }
        return dbToClassName(className);
    }

    /**
     * 截取表备注-> 符号后配置的包路径
     *
     * @return
     */
    public static String getPackagePath(String path, String comment) {
        String expandPackage = getPackagePath(comment);
        if (expandPackage != null && expandPackage.length() > 0) {
            return path + "." + expandPackage;
        }
        return path;
    }

    public static String getPackagePath(String comment) {
        comment = BeanUtil.replaceBlank(comment);
        if (comment != null && comment.lastIndexOf("->") > 0) {
            return comment.substring(comment.lastIndexOf("->") + 2);
        }
        return "";
    }

    /**
     * 获取表备注
     *
     * @return
     */
    public static String getComment(String comment) {
        comment = BeanUtil.replaceBlank(comment);
        if (comment != null && comment.lastIndexOf("->") > 0) {
            return comment.substring(0, comment.lastIndexOf("->"));
        } else if (comment == null) {
            return "";
        }
        return comment;
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
        if (str.length() == 0 || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }


    /**
     * 获取null字符串判断
     *
     * @param str
     * @return
     */
    public static String getString(String str) {
        //StringUtils.hasLength(str) 为true时返回str
        if (StringUtils.hasLength(str)) {
            return str;
        }
        return "";
    }

    /**
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
