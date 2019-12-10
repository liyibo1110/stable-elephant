package com.github.liyibo1110.stable.elephant.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class DynamicDao {

	@Autowired
	@Qualifier("JdbcOperationsList")
	protected List<List<JdbcOperations>> jdbcOperationsList;
	
	private JdbcOperations getJdbcOperations(int index, boolean source) {
		List<JdbcOperations> list = jdbcOperationsList.get(index);
		if(source) {
			return list.get(0);
		}else {
			return list.get(1);
		}
	}
	
	public Long getCountTableMaxId(int index, String schemaName, String countTableName) {
		String sql = "SELECT max_id FROM " + schemaName + "." + countTableName + " LIMIT 1";
		return getJdbcOperations(index, true).queryForObject(sql, Long.class);
	}
}
