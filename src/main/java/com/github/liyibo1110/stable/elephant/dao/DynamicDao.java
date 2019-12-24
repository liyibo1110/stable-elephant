package com.github.liyibo1110.stable.elephant.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.github.liyibo1110.stable.elephant.app.Application;
import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;
import com.github.liyibo1110.stable.elephant.handler.Handler;
import com.github.liyibo1110.stable.elephant.util.Utils;

@Repository
public class DynamicDao {

	private static Logger logger = LoggerFactory.getLogger(DynamicDao.class);
	
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
		// logger.info(sql);
		return getJdbcOperations(index, true).queryForObject(sql, Long.class);
	}
	
	public int updateCountTableMaxId(int index, String schemaName, String countTableName, long maxId) {
		String sql = "UPDATE " + schemaName + "." + countTableName + " SET max_id=?";
		return getJdbcOperations(index, true).update(sql, maxId);
	}
	
	public List<Map<String, Object>> getList(int index, String schemaName, 
					String tableName, ColumnsInfo columnsInfo, 
					long maxId, int limit) {
		
		StringBuilder sb = new StringBuilder();
		// 生成列字符串
		for(String columnName : columnsInfo.getColumnNames()) {
			sb.append(columnName);
			sb.append(",");
		}
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		String sql = "SELECT " + sb.toString() + " FROM " + schemaName + "." + tableName + " WHERE id>" + maxId + " ORDER BY id ASC LIMIT " + limit; 
		// logger.info("sql：" + sql);
		return getJdbcOperations(index, true).queryForList(sql);
	}
	
	public int addList(int index, String schemaName, 
				String tableName, ColumnsInfo columnsInfo, 
				final List<Map<String, Object>> list){  
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		// 生成列字符串
		for(String columnName : columnsInfo.getColumnNames()) {
			sb.append(columnName);
			sb.append(",");
		}
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		// 生成值占位符字符串
		for(String columnType : columnsInfo.getColumnTypes()) {
			sb2.append(Utils.getValuePlaceHolderByColumnType(columnType));
		}
		if(sb2.length() != 0) {
			sb2.deleteCharAt(sb2.length() - 1);
		}
		String sql = "INSERT INTO " + schemaName + "." + tableName +" (" + sb.toString() + ") values(" + sb2.toString() + ")";
		// logger.info("insertSql：" + sql);
		// logger.info("list大小为：" + list.size());
		int[] resultArray = getJdbcOperations(index, false).batchUpdate(sql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Map<String, Object> map = list.get(index);
				int count = 1;
				for(String columnName : map.keySet()) {
					String columnType = columnsInfo.getTypeNameByColumnName(columnName);
					String columnHandler = columnsInfo.getHandlerNameByColumnName(columnName);
					// logger.info("columnType：" + columnType);
					switch(columnType) {
						case "int4": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.INTEGER);
							} else {
								ps.setInt(count, Integer.parseInt(map.get(columnName).toString()));
							}
							
							break;
						}
						case "int4[]": 
						case "int8[]":
						case "numeric[]":
						case "varchar[]": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.ARRAY);
							} else {
								// ps.setString(count, map.get(columnName).toString().replaceAll("\\{|\\}", ""));
								ps.setString(count, map.get(columnName).toString());
							}
							
							break;
						}
						case "int8": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.BIGINT);
							} else {
								ps.setLong(count, Long.parseLong(map.get(columnName).toString()));
							}
							break;
						}
						case "numeric": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.NUMERIC);
							} else {
								ps.setDouble(count, Double.parseDouble(map.get(columnName).toString()));
							}
							break;
						}
						case "varchar": 
						case "text": 
						case "date": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.VARCHAR);
							} else {
								String value = map.get(columnName).toString();
								// logger.info("原值：" + value);
								if(StringUtils.isNotBlank(columnHandler)) {
									// logger.info("columnHandler: " + columnHandler);
									Handler<String> h = (Handler<String>)Application.handlersMap.get(columnHandler);
									value = h.handler(value);
								}
								// logger.info("新值：" + value);
								ps.setString(count, value);
							}
							break;
						}
						case "bool": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.BOOLEAN);
							} else {
								ps.setBoolean(count, Boolean.valueOf(map.get(columnName).toString()));
							}
							break;
						}
						case "jsonb": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.VARCHAR);
							} else {
								ps.setString(count, map.get(columnName).toString());
							}
							break;
						}
						case "timestamp": {
							if (map.get(columnName) == null) {
								ps.setNull(count, java.sql.Types.TIMESTAMP);
							} else {
								ps.setString(count, map.get(columnName).toString());
							}
							break;
						}
						default: {
							throw new IllegalStateException("未找到正确的columnType");
						}
					}
					count++;
				}
			}
        
			public int getBatchSize() {
				return list.size();
			}
		});  
	
		int result = 0;
		for(int i : resultArray){
			result += i;
		}
	
		return result;
	}
	
}
