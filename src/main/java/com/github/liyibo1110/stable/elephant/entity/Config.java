package com.github.liyibo1110.stable.elephant.entity;

import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Config {

	private String cron;
	private Boolean cronEnabled;
	private List<ColumnConvertHandler> columnConvertHandlers;
	private List<AfterQueryHandler> afterQueryHandlers;
	private List<DatabasePair> databasePairs;

	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public Boolean getCronEnabled() {
		return cronEnabled;
	}
	public void setCronEnabled(Boolean cronEnabled) {
		this.cronEnabled = cronEnabled;
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
