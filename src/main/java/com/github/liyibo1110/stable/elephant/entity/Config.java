package com.github.liyibo1110.stable.elephant.entity;

import java.util.List;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Config {

	private List<DatabasePair> databasePairs;

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
