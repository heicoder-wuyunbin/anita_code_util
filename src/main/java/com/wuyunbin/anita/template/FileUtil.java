package com.wuyunbin.anita.template;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wuyunbin
 */
@Slf4j
public class FileUtil {

    /**
     * tem
     * 获取文件内容
     *
     * @param fileName
     * @return
     */
    public static String getContent(String fileName) {
        File file = new File(fileName);
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
            BufferedReader read = new BufferedReader(isr);

            String s;
            while ((s = read.readLine()) != null) {
                result.append(s).append("\n");
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    /**
     * 设置文件内容
     *
     * @param filePath 文件存储目录
     * @param content  文件内容
     */
    public static void setContent(String filePath, String content) {

        try {
            File f = new File(filePath);

            if (f.exists()) {
                f.delete();
            }
            File path = f.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            // 不存在则创建
            f.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(f);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));

            writer.write(content);
            writer.close();
            log.info("生成代码：" + filePath);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 判断文件是否为模板类文件
     *
     * @param
     * @return
     */
    public static boolean isTemplateFile(String filename) {
        String extensionName = getExtensionName(filename);
        if ("html".equals(extensionName)) {
            return true;
        }
        if ("java".equals(extensionName)) {
            return true;
        }
        if ("xml".equals(extensionName)) {
            return true;
        }
        if ("htm".equals(extensionName)) {
            return true;
        }
        if ("jsp".equals(extensionName)) {
            return true;
        }
        if ("project".equals(extensionName)) {
            return true;
        }
        if ("component".equals(extensionName)) {
            return true;
        }
        if (extensionName.indexOf("(TFILE)") > 0) {
            return true;
        }
        return false;
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                // 文件存在时
                //创建新的文件的文件夹
                File newpath = new File(newPath);
                if (!newpath.getParentFile().exists()) {
                    newpath.getParentFile().mkdirs();
                }
                // 读入原文件
                InputStream inStream = Files.newInputStream(Paths.get(oldPath));
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                //int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            log.info("复制单个文件操作出错!原文件:" + oldPath + " 新文件：" + newPath);
            e.printStackTrace();
        }

    }

}
