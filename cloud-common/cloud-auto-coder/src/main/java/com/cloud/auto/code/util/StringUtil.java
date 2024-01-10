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
     * 截取表备注-> 符号后配置的包路径
     *
     * @return
     */
    public static String getPackagePath(String path, String comment) {
        String expandPackage = getPackagePath(comment);
        if (!expandPackage.isEmpty()) {
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
            return Character.toLowerCase(str.charAt(0)) + str.substring(1);
        }
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        if (str.isEmpty() || Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
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


    public static boolean isBlank(@Nullable String string) {
        if (isEmpty(string)) {
            return true;
        } else {
            for (int i = 0; i < string.length(); ++i) {
                if (!Character.isWhitespace(string.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
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
     * @param chineseStr
     * @return
     */
    public static Set<String> getAllPinyin(String chineseStr) {
        //输出格式设置
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        /**
         * 输出大小写设置
         *
         * LOWERCASE:输出小写
         * UPPERCASE:输出大写
         */
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        /**
         * 输出音标设置
         *
         * WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常）
         * WITH_TONE_NUMBER：1-4数字表示音标
         * WITHOUT_TONE：没有音标
         */
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        /**
         * 特殊音标ü设置
         *
         * WITH_V：用v表示ü
         * WITH_U_AND_COLON：用"u:"表示ü
         * WITH_U_UNICODE：直接用ü
         */
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] hanYuArr = chineseStr.trim().toCharArray();
        Set<String> nameSet = new LinkedHashSet<>();
        try {
            for (int i = 0, len = hanYuArr.length; i < len; i++) {
                //匹配是否是汉字
                if (Character.toString(hanYuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    //如果是多音字，返回多个拼音
                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[i], format);
                    if (nameSet.isEmpty()) {
                        nameSet.addAll(Arrays.asList(pys));
                    } else {
                        Set<String> term = new HashSet<>();
                        for (String a1 : nameSet) {
                            for (String a2 : pys) {
                                term.add(a1 + a2);
                            }
                        }
                        nameSet = term;
                    }
                } else {
                    Set<String> term = new HashSet<>();
                    for (String a : nameSet) {
                        term.add(a + hanYuArr[i]);
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
