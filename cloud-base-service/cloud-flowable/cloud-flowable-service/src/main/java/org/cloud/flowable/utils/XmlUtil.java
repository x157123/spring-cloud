package org.cloud.flowable.utils;

import org.apache.commons.jexl3.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlUtil {

    /**
     * 获取元素中的名称 id
     *
     * @param xml
     * @return
     */
    public static Map<String, String> getXmlMsg(String xml) {
        Map<String, String> map = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));

            // 获取文档的根元素
            Element rootElement = doc.getDocumentElement();

            // 获取process元素
            NodeList processList = rootElement.getElementsByTagName("process");

            if (processList != null && processList.getLength() > 0) {
                Element processElement = (Element) processList.item(0);

                // 读取id属性
                String id = processElement.getAttribute("id");
                System.out.println("Process ID: " + id);

                // 读取name属性
                String name = processElement.getAttribute("name");
                System.out.println("Process Name: " + name);

                map.put("id", id);
                map.put("name", name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public static boolean expression(String str, Map<String, Object> data) {
        // 创建 GraalVM 的 JavaScript 上下文
        try {
            // 创建 JexlEngine 实例
            JexlEngine jexl = new JexlBuilder().create();

            // 创建 JexlExpression 实例，这里的表达式是 "${day > 1}"
            JexlExpression expression = jexl.createExpression(extractExpression(str));

            // 准备一个包含变量的上下文
            JexlContext context = new MapContext();

            data.forEach((k, v) -> {
                context.set(k, v);
            });
//            context.set("day", 1);  // 设置变量 day 的值
//            context.set("outcome", "通过");  // 设置变量 day 的值

            // 执行表达式并获取结果
            Boolean result = (Boolean) expression.evaluate(context);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    private static String extractExpression(String input) {
        // 定义正则表达式
        String regex = "\\$\\{([^}]*)\\}";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 匹配输入字符串
        Matcher matcher = pattern.matcher(input);

        // 查找匹配
        if (matcher.find()) {
            // 提取第一个捕获组的内容
            return matcher.group(1);
        }

        return null; // 如果没有匹配到，则返回null或抛出异常，取决于你的需求
    }
}
