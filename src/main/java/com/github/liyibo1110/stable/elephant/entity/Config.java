package com.github.liyibo1110.stable.elephant.entity;

import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Config {

	private String whenHour;
	private String whenMinute;
	private List<ColumnConvertHandler> columnConvertHandlers;
	private List<AfterQueryHandler> afterQueryHandlers;
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
	public List<ColumnConvertHandler> getColumnConvertHandlers() {
		return columnConvertHandlers;
	}
	public void setColumnConvertHandlers(List<ColumnConvertHandler> columnConvertHandlers) {
		this.columnConvertHandlers = columnConvertHandlers;
	}
	public List<AfterQueryHandler> getAfterQueryHandlers() {
		return afterQueryHandlers;
	}
	public void setAfterQueryHandlers(List<AfterQueryHandler> afterQueryHandlers) {
		this.afterQueryHandlers = afterQueryHandlers;
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
