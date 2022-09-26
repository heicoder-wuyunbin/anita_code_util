package com.wuyunbin.anita.template;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuyunbin.anita.entity.Column;
import com.wuyunbin.anita.entity.Table;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板处理类 用于替换内容
 *
 * @author wuyunbin
 */
@Slf4j
public class ClientTemplateUtil {

    /**
     * 根据目录查找所有子模板
     *
     * @param basePath 模版文件目录
     * @return 模版列表
     */
    public static Map<String, String> getTemplateList(String basePath) {
        Map<String, String> map = new HashMap<>();

        //递归显示文件夹下所有文件夹及其中文件
        File root = new File(basePath);
        try {
            map = showAllFiles(basePath, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    static Map<String, String> showAllFiles(String basePath, File dir) {

        Map<String, String> map = new HashMap<>();

        File[] fs = dir.listFiles();
        if (fs == null) {
            return map;
        }
        for (File f : fs) {
            File file = new File(f.getAbsolutePath());
            //将读取的子模板的内容放在map集合中

            if (f.isDirectory()) {
                try {
                    map.putAll(showAllFiles(basePath, f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                map.put(file.getName(), FileUtil.getContent(f.getAbsolutePath()));
            }
        }
        return map;
    }


    /**
     * 替换列级模板
     *
     * @param oldContent 原文本
     * @param map 子替换符号列表
     * @return 替换后的文本
     */
    public static String createContent(String oldContent, Map<String, String> map, Table table) {
        //循环所有子替换符
        for (String ks : map.keySet()) {
            //替换符号
            String thf = "<" + ks + ">";

            if (oldContent.contains(thf)) {
                //循环体
                String foreachContent = map.get(ks);

                StringBuilder createContent = new StringBuilder();

                for (Column column : table.getColumns()) {
                    //控制开关
                    boolean b = true;
                    b = checkKey(ks, column, b);
                    //根据模板生成新内容
                    if (b) {
                        String newContent = foreachContent.replace("[column]", column.getColumnName());
                        newContent = newContent.replace("[Column]", Utils.getClassName(column.getColumnName()));

                        newContent = newContent.replace("[column2]", column.getColumnName2());
                        newContent = newContent.replace("[Column2]", Utils.getClassName(column.getColumnName2()));
                        //java类型
                        newContent = newContent.replace("[type]", column.getColumnType());
                        //数据库类型
                        newContent = newContent.replace("[dbtype]", column.getColumnDbType());
                        if (column.getColumnComment() == null || "".equals(column.getColumnComment())) {
                            //设置为名称
                            column.setColumnComment(column.getColumnName());
                        }
                        //备注
                        newContent = newContent.replace("[columnComment]", column.getColumnComment());
                        createContent.append(newContent);
                    }
                }
                //替换主体内容
                oldContent = oldContent.replace(thf, createContent.toString());
            }
        }

        oldContent = oldContent.replace("[table]", table.getName());
        oldContent = oldContent.replace("[Table]", Utils.getClassName(table.getName()));
        oldContent = oldContent.replace("[table2]", table.getName2());
        oldContent = oldContent.replace("[Table2]", Utils.getClassName(table.getName2()));

        if (table.getComment() == null || "".equals(table.getComment())) {
            table.setComment(table.getName2());
        }
        //备注
        oldContent = oldContent.replace("[comment]", table.getComment());

        if (table.getKey() != null) {
            // 主键
            oldContent = oldContent.replace("[key]", table.getKey());
        }
        if (table.getKey2() != null) {
            //主键驼峰
            oldContent = oldContent.replace("[key2]", table.getKey2());
        }
        if (table.getKeyType() != null) {
            //主键类型
            oldContent = oldContent.replace("[keyType]", table.getKeyType());
        }
        if (table.getKey2Upper() != null) {
            //大写主键
            oldContent = oldContent.replace("[Key2]", table.getKey2Upper());
        }
        return oldContent;
    }

    private static boolean checkKey(String ks, Column column, boolean b) {
        //只循环主键
        if (ks.contains(".key")) {
            if (!"PRI".equals(column.getColumnKey())) {
                //不是主键
                b = false;
            }
        }
        //只循环非主键
        if (ks.contains(".nokey")) {
            if ("PRI".equals(column.getColumnKey())) {
                //不是主键
                b = false;
            }
        }

        //只循环String 类型
        if (ks.contains(".String")) {
            if (!"String".equals(column.getColumnType())) {
                //不是String
                b = false;
            }
        }

        //只循环String 类型
        if (ks.contains(".Integer")) {
            if (!"Integer".equals(column.getColumnType())) {
                //不是Integer
                b = false;
            }
        }

        //只循环Long类型
        if (ks.contains(".Long")) {
            if (!"Long".equals(column.getColumnType())) {
                //不是Long
                b = false;
            }
        }

        //只循环Date 类型
        if (ks.contains(".Date")) {
            if (!"java.util.Date".equals(column.getColumnType())) {
                //不是String
                b = false;
            }
        }
        return b;
    }


    /**
     * 替换表级模板
     *
     * @param oldContent 原文本
     * @param map        子替换符号列表
     * @return
     */
    public static String createContentForTable(String oldContent, Map<String, String> map, List<Table> tables) {
        //循环所有子替换符
        for (String ks : map.keySet()) {
            //替换符号
            String thf = "<" + ks + ">";
            if (oldContent.contains(thf)) {
                //循环体
                String foreachContent = map.get(ks);
                StringBuilder createContent = new StringBuilder();
                for (Table table : tables) {
                    //控制开关
                    boolean b = true;
                    //根据模板生成新内容
                    if (b) {
                        String newContent = foreachContent.replace("[table]", table.getName());
                        newContent = newContent.replace("[Table]", Utils.getClassName(table.getName()));

                        newContent = newContent.replace("[table2]", table.getName2());
                        newContent = newContent.replace("[Table2]", Utils.getClassName(table.getName2()));

                        if (table.getKey() != null) {
                            //备注
                            oldContent = oldContent.replace("[key]", table.getKey());
                        }
                        if (table.getKey2() != null) {
                            //备注
                            oldContent = oldContent.replace("[key2]", table.getKey2());
                        }
                        if (table.getKeyType() != null) {
                            //备注
                            oldContent = oldContent.replace("[keyType]", table.getKeyType());
                        }
                        if (table.getKey2Upper() != null) {
                            //大写主键
                            oldContent = oldContent.replace("[Key2]", table.getKey2Upper());
                        }
                        if (table.getComment() == null || "".equals(table.getComment())) {
                            table.setComment(table.getName2());
                        }
                        //备注
                        newContent = newContent.replace("[comment]", table.getComment());
                        createContent.append(newContent);
                    }
                }
                //替换主体内容
                oldContent = oldContent.replace(thf, createContent.toString());
            }
        }


        return oldContent;
    }


    /**
     * 替换全局替换符
     *
     * @param oldContent
     * @param map
     * @return
     */
    public static String createContent(String oldContent, Map<String, String> map) {
        //循环所有子替换符
        for (String ks : map.keySet()) {
            oldContent = oldContent.replace("[" + ks + "]", map.get(ks));
        }

        return oldContent;
    }

}
