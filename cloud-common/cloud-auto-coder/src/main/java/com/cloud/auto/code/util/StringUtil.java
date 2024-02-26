package com.cloud.auto.code.util;

import com.cloud.common.core.utils.BeanUtil;
import io.micrometer.common.lang.Nullable;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.util.StringUtils;

import java.util.*;
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
     * @param dbName 数据库字段名
     * @return 转换后的java类名
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
     * 获取类名
     *
     * @param name    类名
     * @return 类名
     */
    public static String getClassName(String name) {
        return getClassName(name, null);
    }



    /**
     * 获取类名
     *
     * @param name    类名
     * @param prefix  类名前缀
     * @return 类名
     */
    public static String getClassName(String name, List<String> prefix) {
        String className = name;
        if (prefix != null && !prefix.isEmpty()) {
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
     * 截取表备注中的包路径
     *
     * @param path    包路径
     * @param comment 表备注
     * @return 包路径
     */
    public static String getPackagePath(String path, String comment) {
        String expandPackage = getPackagePath(comment);
        if (!expandPackage.isEmpty()) {
            return path + "." + expandPackage;
        }
        return path;
    }

    /**
     * 获取表备注中的包路径
     *
     * @param comment 表备注
     * @return 包路径
     */
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
     * @param commentData 表备注
     * @return 表备注
     */
    public static String getComment(String commentData) {
        String comment = BeanUtil.replaceBlank(commentData);
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
     * @param str 字符串
     * @return 首字母小写的字符串
     */
    public static String toLowerCaseFirstOne(String str) {
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }

    /**
     * 首字母转大写
     *
     * @param str 字符串
     * @return 首字母大写的字符串
     */
    public static String toUpperCaseFirstOne(String str) {
        if (str.isEmpty() || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    }

    /**
     * 获取非空字符串
     *
     * @param str 字符串
     * @return 非空字符串
     */
    public static String getString(String str) {
        if (StringUtils.hasLength(str)) {
            return str;
        }
        return "";
    }

    /**
     * 去除字符串中的空白字符
     *
     * @param str 字符串
     * @return 去除空白字符后的字符串
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isBlank(@Nullable String string) {
        return isEmpty(string) || string.trim().isEmpty();
    }

    public static boolean isNotBlank(@Nullable String string) {
        return !isBlank(string);
    }

    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNotEmpty(@Nullable String string) {
        return !isEmpty(string);
    }

    public static String truncate(String string, int maxLength) {
        return string.length() > maxLength ? string.substring(0, maxLength) : string;
    }

    public static String truncate(String string, int maxLength, String truncationIndicator) {
        if (truncationIndicator.length() >= maxLength) {
            throw new IllegalArgumentException("maxLength must be greater than length of truncationIndicator");
        } else if (string.length() > maxLength) {
            int remainingLength = maxLength - truncationIndicator.length();
            return string.substring(0, remainingLength) + truncationIndicator;
        } else {
            return string;
        }
    }

    /**
     * 中文转拼音（大写，无音标）
     *
     * @param chineseStr 中文字符串
     * @param append     拼音间的分隔符
     * @return 拼音集合
     */
    public static Set<String> getAllPinyin(String chineseStr, String append) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] hanYuArr = chineseStr.trim().toCharArray();
        Set<String> nameSet = new LinkedHashSet<>();
        try {
            for (char c : hanYuArr) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (nameSet.isEmpty()) {
                        nameSet.addAll(Arrays.asList(pys));
                    } else {
                        Set<String> term = new HashSet<>();
                        for (String a1 : nameSet) {
                            for (String a2 : pys) {
                                term.add(a1 + append + a2);
                            }
                        }
                        nameSet = term;
                    }
                } else {
                    Set<String> term = new HashSet<>();
                    for (String a : nameSet) {
                        term.add(a + c);
                    }
                    nameSet = term;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return nameSet;
    }
}