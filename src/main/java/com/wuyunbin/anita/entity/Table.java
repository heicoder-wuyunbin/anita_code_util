package com.wuyunbin.anita.entity;

import lombok.Data;

import java.util.List;
/**
 * 表实体
 * @author wuyunbin
 *
 */
@Data
public class Table {
	/**
	 * 表名称
	 */
	private String name;
	/**
	 * 处理后的表名称
	 */
	private String name2;
	/**
	 * 介绍
	 */
	private String comment;
	/**
	 * 主键列
	 */
	private String key;
	/**
	 * 主键列（驼峰）
	 */
	private String key2;
	/**
	 * 主键列（驼峰）
	 */
	private String key2Upper;
	/**
	 * 主键类型
	 */
	private String keyType;
	/**
	 * 列集合
	 */
	private List<Column> columns;
}
