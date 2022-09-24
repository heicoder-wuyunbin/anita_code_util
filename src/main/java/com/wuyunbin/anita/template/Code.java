package com.wuyunbin.anita.template;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.wuyunbin.anita.entity.Table;
import com.wuyunbin.anita.entity.Template;
import com.wuyunbin.anita.xml.DatabaseXml;
import lombok.extern.slf4j.Slf4j;

/**
 * 代码相关操作类
 *
 * @author wuyunbin
 */
@Slf4j
public class Code {

    /**
     * 代码生成
     *
     * @param pathMap   路径参数封装
     * @param publicMap 全局替换符
     */
    public static void create(Map<String, String> pathMap, Map<String, String> publicMap) {
        //变量定义
        //框架模板所在目录
        String projectTempletPath = pathMap.get("projectTempletPath");
        //表级模板所在目录
        String tablleTempletPath = pathMap.get("tablleTempletPath");
        //列级模板所在目录
        String columnTempletPath = pathMap.get("columnTempletPath");
        //数据库信息文件
        String xmlPath = pathMap.get("xmlPath");
        //代码输出目录
        String codePath = pathMap.get("codePath");
        log.info("xmlPath="+xmlPath);
        //追加到全局替换符******************
        Map<String, String> propertyMap = DatabaseXml.readProperty(xmlPath);
        publicMap.putAll(propertyMap);
        //******************************
        //得到表的列表
        List<Table> tableList = DatabaseXml.readDatabaseXml(xmlPath);
        //表级模板MAP
        Map<String, String> tableTemplateMap = ClientTemplateUtil.getTemplateList(tablleTempletPath);
        //列级模板MAP
        Map<String, String> columnTemplateMap = ClientTemplateUtil.getTemplateList(columnTempletPath);
        List<Template> list = TemplateUtil.getTempletList(projectTempletPath);
        //循环所有模板
        for (Template t : list) {
            log.info("处理模板：" + t.getPath() + "   --- " + t.getFileName());
            //如果是模板类文件
            if (FileUtil.isTemplateFile(t.getAllPath())) {
                //读取模板文件内容
                String content = FileUtil.getContent(t.getAllPath());

                //替换表级模板部分
                content = ClientTemplateUtil.createContentForTable(content, tableTemplateMap, tableList);

                //如果文件名包含表替换符号则循环输出
                if (t.getFileName().contains("[table]") ||
                        t.getFileName().contains("[Table]") ||
                        t.getFileName().contains("[table2]") ||
                        t.getFileName().contains("[Table2]")) {

                    for (Table table : tableList) {
                        //输出的文件名
                        String outFile = t.getFileName().replace("[table]", table.getName());
                        outFile = outFile.replace("[Table]", Utils.getClassName(table.getName()));
                        outFile = outFile.replace("[Table2]", Utils.getClassName(table.getName2()));
                        outFile = outFile.replace("[table2]", table.getName2());
                        //得到模板内容
                        String outContent = content;

                        //替换列级模板部分
                        outContent = ClientTemplateUtil.createContent(outContent, columnTemplateMap, table);

                        //全局替换符嵌套替换符处理********
                        log.info("全局替换符嵌套替换符处理********" + table.getName());

                        //全局替换
                        outContent = ClientTemplateUtil.createContent(outContent, publicMap);

                        outContent = outContent.replace("[table]", table.getName());

                        //输出的路径(经过全局替换)
                        String outPath = ClientTemplateUtil.createContent(codePath + t.getPath() + File.separatorChar + outFile, publicMap);
                        //在新的文件中去掉模板文件标记***********(TFILE)
                        outPath = outPath.replace("(TFILE)", "");
                        //写入文件
                        FileUtil.setContent(outPath, outContent);
                    }
                } else {
                    //不用循环的文件
                    String outPath = ClientTemplateUtil.createContent(codePath + t.getPath() + File.separatorChar + t.getFileName(), publicMap);
                    //在新的文件中去掉模板文件标记***********(TFILE)
                    outPath = outPath.replace("(TFILE)", "");

                    content = ClientTemplateUtil.createContent(content, publicMap);
                    //写入文件
                    FileUtil.setContent(outPath, content);
                }
            } else {
                //非模板文件直接拷贝
                String newPath = ClientTemplateUtil.createContent(codePath + File.separatorChar + t.getPath() + File.separatorChar + t.getFileName(), publicMap);

                FileUtil.copyFile(t.getAllPath(), newPath);
            }
        }
        log.info("代码成功生成!");
    }

}
