package com.github.liyibo1110.stable.elephant.entity;

import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class DatabasePair {

	private String name;
	private Boolean enabled;
	private SourceDatabase sourceDatabase;
	private TargetDatabase targetDatabase;
	private List<Table> tables;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public SourceDatabase getSourceDatabase() {
		return sourceDatabase;
	}
	public void setSourceDatabase(SourceDatabase sourceDatabase) {
		this.sourceDatabase = sourceDatabase;
	}
	public TargetDatabase getTargetDatabase() {
		return targetDatabase;
	}
	public void setTargetDatabase(TargetDatabase targetDatabase) {
		this.targetDatabase = targetDatabase;
	}
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, MultilineRecursiveToStringStyle.JSON_STYLE);
	}
}
