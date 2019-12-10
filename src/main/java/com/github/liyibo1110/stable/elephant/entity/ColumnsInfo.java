package com.github.liyibo1110.stable.elephant.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ColumnsInfo {

	private List<String> columnNames = new ArrayList<>();
	private List<String> columnTypes = new ArrayList<>();
	
	public void addColumnName(String columnName) {
		this.columnNames.add(columnName);
	}
	
	public void addColumnType(String columnType) {
		this.columnTypes.add(columnType);
	}
	
	public List<String> getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	public List<String> getColumnTypes() {
		return columnTypes;
	}
	public void setColumnTypes(List<String> columnTypes) {
		this.columnTypes = columnTypes;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.MULTI_LINE_STYLE);
	}
}
