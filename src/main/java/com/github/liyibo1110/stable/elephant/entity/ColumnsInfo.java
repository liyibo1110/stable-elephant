package com.github.liyibo1110.stable.elephant.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ColumnsInfo {

	private List<String> columnNames = new ArrayList<>();
	private List<String> columnTypes = new ArrayList<>();
	private Map<String, String> nameAndTypeMap = new HashMap<>();
	
	public void addColumnName(String columnName) {
		this.columnNames.add(columnName);
	}
	
	public void addColumnType(String columnType) {
		this.columnTypes.add(columnType);
	}
	
	public void putColumnNameAndColumnType(String columnName, String columnType) {
		this.nameAndTypeMap.put(columnName, columnType);
	}
	
	public String getTypeNameByColumnName(String columnName) {
		return nameAndTypeMap.get(columnName);
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
	
	public Map<String, String> getNameAndTypeMap() {
		return nameAndTypeMap;
	}

	public void setNameAndTypeMap(Map<String, String> nameAndTypeMap) {
		this.nameAndTypeMap = nameAndTypeMap;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.MULTI_LINE_STYLE);
	}
}
