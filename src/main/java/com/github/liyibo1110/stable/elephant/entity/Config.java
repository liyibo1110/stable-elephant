package com.github.liyibo1110.stable.elephant.entity;

import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Config {

	private String whenHour;
	private String whenMinute;
	private List<ColumnHandler> columnHandlers;
	private List<DatabasePair> databasePairs;

	public String getWhenHour() {
		return whenHour;
	}

	public void setWhenHour(String whenHour) {
		this.whenHour = whenHour;
	}
	
	public String getWhenMinute() {
		return whenMinute;
	}

	public void setWhenMinute(String whenMinute) {
		this.whenMinute = whenMinute;
	}

	public List<ColumnHandler> getColumnHandlers() {
		return columnHandlers;
	}

	public void setColumnHandlers(List<ColumnHandler> columnHandlers) {
		this.columnHandlers = columnHandlers;
	}

	public List<DatabasePair> getDatabasePairs() {
		return databasePairs;
	}

	public void setDatabasePairs(List<DatabasePair> databasePairs) {
		this.databasePairs = databasePairs;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.JSON_STYLE);
	}
	
}
