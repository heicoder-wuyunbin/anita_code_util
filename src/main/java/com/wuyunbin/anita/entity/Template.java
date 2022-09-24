package com.wuyunbin.anita.entity;

import lombok.Data;

/**
 * 模板文件实体
 * @author wuyunbin
 *
 */
@Data
public class Template {
	/**
	 * 生成路径
	 */
	private String path;
	/**
	 * 模板文件名
	 */
	private String fileName;
	/**
	 * 完成路径
	 */
	private String allPath;

}
