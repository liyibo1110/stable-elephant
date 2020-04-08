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
	private List<String> columnConvertHandlers = new ArrayList<>();
	private List<String> columnJoinTable = new ArrayList<>();
	private List<String> columnSelfColumn = new ArrayList<>();
	private List<String> columnReferColumn = new ArrayList<>();
	
	private Map<String, String> nameAndTypeMap = new HashMap<>();
	private Map<String, String> nameAndConvertHandlerMap = new HashMap<>();
	private Map<String, Boolean> nameAndNeedInsertMap = new HashMap<>();
	
	public void addColumnName(String columnName) {
		this.columnNames.add(columnName);
	}
	public void addColumnType(String columnType) {
		this.columnTypes.add(columnType);
	}
	public void addColumnConvertHandlers(String columnConvertHandlers) {
		this.columnConvertHandlers.add(columnConvertHandlers);
	}
	public void addColumnJoinTable(String columnJoinTable) {
		this.columnJoinTable.add(columnJoinTable);
	}
	public void addColumnSelfColumn(String columnSelfColumn) {
		this.columnSelfColumn.add(columnSelfColumn);
	}
	public void addColumnReferColumn(String columnReferColumn) {
		this.columnReferColumn.add(columnReferColumn);
	}
	public void putColumnNameAndColumnType(String columnName, String columnType) {
		this.nameAndTypeMap.put(columnName, columnType);
	}
	public void putColumnNameAndColumnConvertHandler(String columnName, String columnConvertHandler) {
		this.nameAndConvertHandlerMap.put(columnName, columnConvertHandler);
	}
	public void putColumnNameAndNeedInsertHandler(String columnName, Boolean needInsert) {
		this.nameAndNeedInsertMap.put(columnName, needInsert);
	}
	public String getTypeNameByColumnName(String columnName) {
		return nameAndTypeMap.get(columnName);
	}
	public String getConvertHandlerNameByColumnName(String columnName) {
		return nameAndConvertHandlerMap.get(columnName);
	}
	public Boolean getNeedInsertByColumnName(String columnName) {
		return nameAndNeedInsertMap.get(columnName);
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
	public List<String> getColumnConvertHandlers() {
		return columnConvertHandlers;
	}
	public void setColumnConvertHandlers(List<String> columnConvertHandlers) {
		this.columnConvertHandlers = columnConvertHandlers;
	}
	public List<String> getColumnJoinTable() {
		return columnJoinTable;
	}
	public void setColumnJoinTable(List<String> columnJoinTable) {
		this.columnJoinTable = columnJoinTable;
	}
	public List<String> getColumnSelfColumn() {
		return columnSelfColumn;
	}
	public void setColumnSelfColumn(List<String> columnSelfColumn) {
		this.columnSelfColumn = columnSelfColumn;
	}
	public List<String> getColumnReferColumn() {
		return columnReferColumn;
	}
	public void setColumnReferColumn(List<String> columnReferColumn) {
		this.columnReferColumn = columnReferColumn;
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
