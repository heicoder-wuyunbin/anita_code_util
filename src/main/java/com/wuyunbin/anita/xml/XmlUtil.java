package com.wuyunbin.anita.xml;

import cn.hutool.core.text.UnicodeUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyunbin
 */
public class XmlUtil {

    public static void write(Map<String, String> map, String filename) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("utf-8");

        Element root = doc.addElement("root");
        for (String key : map.keySet()) {

            Element mapElement = root.addElement("map");
            mapElement.addAttribute("key", key);
            mapElement.addAttribute("value", UnicodeUtil.toUnicode(map.get(key)));
        }
        //设定为当前文件夹
        File directory = new File("");
        writeXml(directory.getAbsolutePath() + File.separatorChar + filename, doc);
    }


    public static Map<String, String> read(String filename) {
        //设定为当前文件夹
        File directory = new File("");
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        //读取类型转换器
        try {
            Document doc = reader.read(directory.getAbsolutePath() + File.separatorChar + filename);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                map.put(element.attributeValue("key"), UnicodeUtil.toString(element.attributeValue("value")));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static Map<String, String> readNu(String filename) {
        //设定为当前文件夹
        File directory = new File("");
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        //读取类型转换器
        try {
            Document doc = reader.read(directory.getAbsolutePath() + File.separatorChar + filename);
            Element rootElement = doc.getRootElement();
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                map.put(element.attributeValue("key"), element.attributeValue("value"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @param outPath
     * @param doc
     */
    private static void writeXml(String outPath, Document doc) {
        try {

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");

            XMLWriter writer = null;

            File file = new File(outPath);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
			e.printStackTrace();
        }
    }

}
