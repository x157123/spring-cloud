package com.cloud.auto.code.util;

import cn.hutool.core.util.StrUtil;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import java.util.ArrayList;
import java.util.List;

public class ChatGPTUtil {
    public static void getCode() {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-333333333333333333333333333")
                .timeout(9000)
                .apiHost("https://api.chatanywhere.tech") //反向代理地址
                .build()
                .init();


        //prompt
        Message system = Message.ofSystem("我是来自中国的用户，帮我添加中文代码注释 可以优化方法中的代码 但是不要删除方法");
        //获取历史消息记录
        List<Message> historyMsgList = new ArrayList<>();
        Message message = Message.of("package com.cloud.auto.code.util;\n" +
                "\n" +
                "import com.cloud.common.core.utils.BeanUtil;\n" +
                "import io.micrometer.common.lang.Nullable;\n" +
                "import net.sourceforge.pinyin4j.PinyinHelper;\n" +
                "import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;\n" +
                "import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;\n" +
                "import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;\n" +
                "import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;\n" +
                "import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;\n" +
                "import org.springframework.util.StringUtils;\n" +
                "\n" +
                "import java.util.*;\n" +
                "import java.util.regex.Matcher;\n" +
                "import java.util.regex.Pattern;\n" +
                "\n" +
                "/**\n" +
                " * 文本工具类\n" +
                " *\n" +
                " * @author liulei\n" +
                " */\n" +
                "public class StringUtil {\n" +
                "\n" +
                "    private static Pattern p = Pattern.compile(\"\\\\s*|\\t|\\r|\\n\");\n" +
                "\n" +
                "    /**\n" +
                "     * 数据库字段转java驼峰命名\n" +
                "     *\n" +
                "     * @param dbName\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String dbToClassName(String dbName) {\n" +
                "        String[] words = dbName.split(\"_\");\n" +
                "        StringBuilder stringBuilder = new StringBuilder(words[0]);\n" +
                "        for (int i = 1; i < words.length; i++) {\n" +
                "            stringBuilder.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1));\n" +
                "        }\n" +
                "        return stringBuilder.toString();\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 获取ClassName\n" +
                "     *\n" +
                "     * @return\n" +
                "     */\n" +
                "\n" +
                "    public static String getClassName(String name) {\n" +
                "        return getClassName(name, null);\n" +
                "    }\n" +
                "\n" +
                "    public static String getClassName(String name, List<String> prefix) {\n" +
                "        String className = name;\n" +
                "        if (prefix != null && !prefix.isEmpty()) {\n" +
                "            for (String pre : prefix) {\n" +
                "                int i = className.indexOf(pre);\n" +
                "                if (i == 0) {\n" +
                "                    className = className.substring(pre.length());\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return dbToClassName(className);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 截取表备注-> 符号后配置的包路径\n" +
                "     *\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String getPackagePath(String path, String comment) {\n" +
                "        String expandPackage = getPackagePath(comment);\n" +
                "        if (!expandPackage.isEmpty()) {\n" +
                "            return path + \".\" + expandPackage;\n" +
                "        }\n" +
                "        return path;\n" +
                "    }\n" +
                "\n" +
                "    public static String getPackagePath(String comment) {\n" +
                "        comment = BeanUtil.replaceBlank(comment);\n" +
                "        if (comment != null && comment.lastIndexOf(\"->\") > 0) {\n" +
                "            return comment.substring(comment.lastIndexOf(\"->\") + 2);\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 获取表备注\n" +
                "     *\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String getComment(String comment) {\n" +
                "        comment = BeanUtil.replaceBlank(comment);\n" +
                "        if (comment != null && comment.lastIndexOf(\"->\") > 0) {\n" +
                "            return comment.substring(0, comment.lastIndexOf(\"->\"));\n" +
                "        } else if (comment == null) {\n" +
                "            return \"\";\n" +
                "        }\n" +
                "        return comment;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 首字母转小写\n" +
                "     *\n" +
                "     * @param str\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String toLowerCaseFirstOne(String str) {\n" +
                "        if (Character.isLowerCase(str.charAt(0))) {\n" +
                "            return str;\n" +
                "        } else {\n" +
                "            return Character.toLowerCase(str.charAt(0)) + str.substring(1);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 首字母转大写\n" +
                "     *\n" +
                "     * @param str\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String toUpperCaseFirstOne(String str) {\n" +
                "        if (str.isEmpty() || Character.isUpperCase(str.charAt(0))) {\n" +
                "            return str;\n" +
                "        } else {\n" +
                "            return Character.toUpperCase(str.charAt(0)) + str.substring(1);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 获取null字符串判断\n" +
                "     *\n" +
                "     * @param str\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String getString(String str) {\n" +
                "        //StringUtils.hasLength(str) 为true时返回str\n" +
                "        if (StringUtils.hasLength(str)) {\n" +
                "            return str;\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * @param str\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static String replaceBlank(String str) {\n" +
                "        String dest = \"\";\n" +
                "        if (str != null) {\n" +
                "            Matcher m = p.matcher(str);\n" +
                "            dest = m.replaceAll(\"\");\n" +
                "        }\n" +
                "        return dest;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public static boolean isBlank(@Nullable String string) {\n" +
                "        if (isEmpty(string)) {\n" +
                "            return true;\n" +
                "        } else {\n" +
                "            for (int i = 0; i < string.length(); ++i) {\n" +
                "                if (!Character.isWhitespace(string.charAt(i))) {\n" +
                "                    return false;\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            return true;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public static boolean isNotBlank(@Nullable String string) {\n" +
                "        return !isBlank(string);\n" +
                "    }\n" +
                "\n" +
                "    public static boolean isEmpty(@Nullable String string) {\n" +
                "        return string == null || string.isEmpty();\n" +
                "    }\n" +
                "\n" +
                "    public static boolean isNotEmpty(@Nullable String string) {\n" +
                "        return !isEmpty(string);\n" +
                "    }\n" +
                "\n" +
                "    public static String truncate(String string, int maxLength) {\n" +
                "        return string.length() > maxLength ? string.substring(0, maxLength) : string;\n" +
                "    }\n" +
                "\n" +
                "    public static String truncate(String string, int maxLength, String truncationIndicator) {\n" +
                "        if (truncationIndicator.length() >= maxLength) {\n" +
                "            throw new IllegalArgumentException(\"maxLength must be greater than length of truncationIndicator\");\n" +
                "        } else if (string.length() > maxLength) {\n" +
                "            int remainingLength = maxLength - truncationIndicator.length();\n" +
                "            return string.substring(0, remainingLength) + truncationIndicator;\n" +
                "        } else {\n" +
                "            return string;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    /**\n" +
                "     * 中文转拼音（大写，无音标）\n" +
                "     *\n" +
                "     * @param chineseStr\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static Set<String> getAllPinyin(String chineseStr, String append) {\n" +
                "        //输出格式设置\n" +
                "        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();\n" +
                "        /**\n" +
                "         * 输出大小写设置\n" +
                "         *\n" +
                "         * LOWERCASE:输出小写\n" +
                "         * UPPERCASE:输出大写\n" +
                "         */\n" +
                "        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);\n" +
                "\n" +
                "        /**\n" +
                "         * 输出音标设置\n" +
                "         *\n" +
                "         * WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常）\n" +
                "         * WITH_TONE_NUMBER：1-4数字表示音标\n" +
                "         * WITHOUT_TONE：没有音标\n" +
                "         */\n" +
                "        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);\n" +
                "\n" +
                "        /**\n" +
                "         * 特殊音标ü设置\n" +
                "         *\n" +
                "         * WITH_V：用v表示ü\n" +
                "         * WITH_U_AND_COLON：用\"u:\"表示ü\n" +
                "         * WITH_U_UNICODE：直接用ü\n" +
                "         */\n" +
                "        format.setVCharType(HanyuPinyinVCharType.WITH_V);\n" +
                "\n" +
                "        char[] hanYuArr = chineseStr.trim().toCharArray();\n" +
                "        Set<String> nameSet = new LinkedHashSet<>();\n" +
                "        try {\n" +
                "            for (int i = 0, len = hanYuArr.length; i < len; i++) {\n" +
                "                //匹配是否是汉字\n" +
                "                if (Character.toString(hanYuArr[i]).matches(\"[\\\\u4E00-\\\\u9FA5]+\")) {\n" +
                "                    //如果是多音字，返回多个拼音\n" +
                "                    String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[i], format);\n" +
                "                    if (nameSet.isEmpty()) {\n" +
                "                        nameSet.addAll(Arrays.asList(pys));\n" +
                "                    } else {\n" +
                "                        Set<String> term = new HashSet<>();\n" +
                "                        for (String a1 : nameSet) {\n" +
                "                            for (String a2 : pys) {\n" +
                "                                term.add(a1 + append + a2);\n" +
                "                            }\n" +
                "                        }\n" +
                "                        nameSet = term;\n" +
                "                    }\n" +
                "                } else {\n" +
                "                    Set<String> term = new HashSet<>();\n" +
                "                    for (String a : nameSet) {\n" +
                "                        term.add(a + hanYuArr[i]);\n" +
                "                    }\n" +
                "                    nameSet = term;\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (BadHanyuPinyinOutputFormatCombination e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return nameSet;\n" +
                "    }\n" +
                "}\n");
        historyMsgList.add(message);
        if(StrUtil.isNotEmpty(system.getContent())){
            historyMsgList.add(0,system);
        }

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
//                .messages(Arrays.asList(system, message))
                .messages(historyMsgList)
                .maxTokens(30000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        System.out.println("ChatGPT返回：" + response);
        Message res = response.getChoices().get(0).getMessage();
        System.out.println("GPT回复：{}" + res.getContent());
    }


    public static void main(String[] args) {
        getCode();
    }

}
