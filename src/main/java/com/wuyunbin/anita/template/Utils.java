package com.wuyunbin.anita.template;

import cn.hutool.core.util.StrUtil;

/**
 * 工具集
 * @author wuyunbin
 *
 */
public class Utils {
		
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String getClassName(String str){
		return StrUtil.upperFirst(StrUtil.toCamelCase(str));
	}

	/**
	 * 去掉表名前缀
	 * @param tableName 表名
	 * @return 去掉表名前缀
	 */
	public static String getTableName2(String tableName,boolean prefix){
		if(prefix){
			//带了前缀
			return StrUtil.subAfter(tableName,"_",false);
		}
		return tableName;
	}


	/**
	 * 下划线转驼峰
	 * @param name 列名
	 * @return 转小驼峰之后的列名
	 */
	public static String getColumnName2(String name){
		return StrUtil.toCamelCase(name);
	}
}
