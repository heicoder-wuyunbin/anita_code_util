package com.wuyunbin.anita.entity;

import lombok.Data;

/**
 * 列对象
 * @author wuyunbin
 *
 */
@Data
public class Column {
	/**
	 * 列名称
	 */
	private String columnName;
	/**
	 * 列名称(处理后的列名称)
	 */
	private String columnName2;
	/**
	 * 列类型
	 */
	private String columnType;
	/**
	 * 列数据库类型
	 */
	private String columnDbType;

	/**
	 * 列备注
	 */
	private String columnComment;
	/**
	 * 是否是主键
	 */
	private String columnKey;
	/**
	 * 小数位数
	 */
	private int decimalDigits;
	/**
	 * COLUMN_SIZE  字段长度
	 */
	private int columnSize;

}
