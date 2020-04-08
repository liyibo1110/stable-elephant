package com.github.liyibo1110.stable.elephant.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Table {

	private String schemaName;
	private String tableName;
	private String afterQueryHandler;
	private List<JoinTable> joinTables = new ArrayList<>();
	private String countTableName;
	/**
	 * 每轮最多获取的数据量（即分页同步）
	 */
	private Integer limit;
	private Boolean enabled;
	private List<Column> columns;

	public String getCountTableName() {
		return countTableName;
	}
	public void setCountTableName(String countTableName) {
		this.countTableName = countTableName;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAfterQueryHandler() {
		return afterQueryHandler;
	}
	public void setAfterQueryHandler(String afterQueryHandler) {
		this.afterQueryHandler = afterQueryHandler;
	}
	public List<JoinTable> getJoinTables() {
		return joinTables;
	}
	public void setJoinTables(List<JoinTable> joinTables) {
		this.joinTables = joinTables;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.JSON_STYLE);
	}
}
