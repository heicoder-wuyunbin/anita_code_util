package com.wuyunbin.anita.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wuyunbin.anita.entity.Template;

/**
 * @author wuyunbin
 */
public class TemplateUtil {

    /**
     * 根据目录查找所有模板文件
     *
     * @param basePath
     * @return
     */
    public static List<Template> getTemplateList(String basePath) {
        List<Template> list = null;

        //递归显示C盘下所有文件夹及其中文件
        File root = new File(basePath);
        try {
            list = showAllFiles(basePath, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    static List<Template> showAllFiles(String basePath, File dir) {
        List<Template> list = new ArrayList<>();

        File[] fs = dir.listFiles();
		for (File f : fs) {

			Template template = new Template();
			//原目录
			template.setAllPath(f.getAbsolutePath());

			File file = new File(f.getAbsolutePath());
			//相对目录
			template.setPath(file.getParent().replace(basePath, ""));
			//文件名
			template.setFileName(file.getName());

			if (f.isDirectory()) {
				try {
					list.addAll(showAllFiles(basePath, f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				list.add(template);
			}
		}
        return list;
    }
}
