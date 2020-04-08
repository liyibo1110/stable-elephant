package com.github.liyibo1110.stable.elephant.entity;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Column {

	private String name;
	private String type;
	private String convertHandler;
	private String joinTable;
	private String selfColumn;
	private String referColumn;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConvertHandler() {
		return convertHandler;
	}
	public void setConvertHandler(String convertHandler) {
		this.convertHandler = convertHandler;
	}
	public String getJoinTable() {
		return joinTable;
	}
	public void setJoinTable(String joinTable) {
		this.joinTable = joinTable;
	}
	public String getSelfColumn() {
		return selfColumn;
	}
	public void setSelfColumn(String selfColumn) {
		this.selfColumn = selfColumn;
	}
	public String getReferColumn() {
		return referColumn;
	}
	public void setReferColumn(String referColumn) {
		this.referColumn = referColumn;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.JSON_STYLE);
	}
}
