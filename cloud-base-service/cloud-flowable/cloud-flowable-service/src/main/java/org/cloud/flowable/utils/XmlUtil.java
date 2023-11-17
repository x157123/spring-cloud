package org.cloud.flowable.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class XmlUtil {

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

                map.put("id",id);
                map.put("name",name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
