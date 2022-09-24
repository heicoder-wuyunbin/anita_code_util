package com.wuyunbin.anita.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.text.UnicodeUtil;
import com.wuyunbin.anita.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.wuyunbin.anita.entity.Column;
import com.wuyunbin.anita.entity.Table;
import com.wuyunbin.anita.template.Utils;

/**
 * 数据库转XML类
 * @author wuyunbin
 */
@Slf4j
public class DatabaseXml {

    /**
     * 写配置文件
     *
     * @param databaseUtil
     * @param outPath
     */
    public static void writeDatabaseXml(DatabaseUtil databaseUtil, Map<String, String> propertyMap, String outPath)  {
        try{
            Document doc = DocumentHelper.createDocument();
            doc.setXMLEncoding("utf-8");

            Element root = doc.addElement("db");
            root.addAttribute("name", databaseUtil.getDbName());
            root.addAttribute("driverName", databaseUtil.getDriverName());
            root.addAttribute("userName", databaseUtil.getUsername());
            root.addAttribute("passWord", databaseUtil.getPassword());
            root.addAttribute("url", databaseUtil.getUrl());
            root.addAttribute("prefix",databaseUtil.getHasPrefix());

            for (String key : propertyMap.keySet()) {
                Element element = root.addElement("property");
                element.addAttribute("name", key);
                element.setText(propertyMap.get(key));
            }

            List<Table> tableList = databaseUtil.getDbInfo();

            for (Table table : tableList) {
                //主键数量
                int keycount = 0;

                String keyType = "";
                for (Column column : table.getColumns()) {
                    if ("PRI".equals(column.getColumnKey())) {
                        keycount++;
                        keyType = column.getColumnType();
                    }
                }
                if (keycount == 1) {
                    //如果是只有一个主键
                    Element tableElement = root.addElement("table");
                    //表名称
                    tableElement.addAttribute("name", table.getName());
                    //处理后的表名称（如果有前缀，去掉前缀）
                    tableElement.addAttribute("name2", Utils.getTableName2(table.getName(), "1".equals(root.attributeValue("prefix"))));
                    tableElement.addAttribute("comment",  UnicodeUtil.toUnicode(table.getComment()));
                    tableElement.addAttribute("key", table.getKey());
                    //转驼峰格式
                    tableElement.addAttribute("key2", Utils.getColumnName2(table.getKey()));
                    //转驼峰格式，首字母大写
                    tableElement.addAttribute("Key2", Utils.getClassName(Utils.getColumnName2(table.getKey())));
                    //主键类型
                    tableElement.addAttribute("keyType", keyType);
                    for (Column column : table.getColumns()) {
                        Element columnElement = tableElement.addElement("column");
                        //字段名称
                        columnElement.addAttribute("name", column.getColumnName());
                        //转驼峰格式
                        columnElement.addAttribute("name2", Utils.getColumnName2(column.getColumnName()));
                        //类型
                        columnElement.addAttribute("type", column.getColumnType());
                        columnElement.addAttribute("dbtype", column.getColumnDbType());
                        columnElement.addAttribute("comment", UnicodeUtil.toUnicode(column.getColumnComment()));
                        columnElement.addAttribute("key", column.getColumnKey());
                        columnElement.addAttribute("decimal_digits", String.valueOf(column.getDecimalDigits()));
                        columnElement.addAttribute("colums_size", String.valueOf(column.getColumnSize()));
                        //主键数量
                        if ("PRI".equals(column.getColumnKey())) {
                            keycount++;
                        }

                    }
                }

            }
            writeXml(outPath, doc);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 返回表名称
     *
     * @return
     */
    public static List<Table> readDatabaseXml(String xmlPath) {
        List<Table> list = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(xmlPath + File.separatorChar + "db.xml");

            Element dbe = doc.getRootElement();
            List<Element> elist = dbe.elements();
            for (Element e : elist) {
                if ("table".equals(e.getName())) {

                    Table table = new Table();
                    table.setName(e.attributeValue("name"));
                    table.setName2(e.attributeValue("name2"));
                    table.setKeyType(e.attributeValue("keyType"));
                    table.setComment(UnicodeUtil.toString(e.attributeValue("comment")));
                    table.setKey(e.attributeValue("key"));
                    table.setKey2(e.attributeValue("key2"));
                    //大写主键名
                    table.setKey2Upper(e.attributeValue("Key2"));
                    List<Column> columns = new ArrayList<>();
                    //字段列表
                    List<Element> elist2 = e.elements();

                    for (Element e2 : elist2) {
                        Column column = new Column();
                        column.setColumnName(e2.attributeValue("name"));
                        column.setColumnName2(e2.attributeValue("name2"));
                        column.setColumnType(e2.attributeValue("type"));
                        column.setColumnDbType(e2.attributeValue("dbtype"));
                        column.setColumnComment(UnicodeUtil.toString(e2.attributeValue("comment")));
                        column.setColumnKey(e2.attributeValue("key"));
                        columns.add(column);
                    }
                    table.setColumns(columns);
                    list.add(table);
                }

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 读取属性
     *
     * @return
     */
    public static Map<String, String> readProperty(String xmlPath) {
        Map<String, String> map = new HashMap<>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(xmlPath + File.separatorChar + "db.xml");

            Element dbe = doc.getRootElement();
            List<Element> elist = dbe.elements();
            for (Element e : elist) {
                if ("property".equals(e.getName())) {
                    map.put(e.attributeValue("name"), e.getText());
                }
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
            String xmlFileName = "db.xml";
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");

            XMLWriter writer = null;

            log.info("db xml path {}",outPath + File.separatorChar + xmlFileName);
            File file = new File(outPath + File.separatorChar + xmlFileName);

            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if(!mkdirs){
                    log.info("文件夹创建失败");
                }
            }

            if(!file.exists()){
                boolean newFile = file.createNewFile();
                if(!newFile){
                    log.info("db.xml文件创建失败");
                }
            }

            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(doc);
            writer.flush();
            writer.close();
            log.info("db.xml 创建完毕");
        } catch (IOException e) {
			e.printStackTrace();
        }
    }
}
