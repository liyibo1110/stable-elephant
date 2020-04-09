package com.github.liyibo1110.stable.elephant.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.github.liyibo1110.stable.elephant.entity.JoinTable;
import com.github.liyibo1110.stable.elephant.entity.Table;
import com.github.liyibo1110.stable.elephant.handler.ConvertHandler;
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
	
	public List<Map<String, Object>> getList(int index, Table table, 
											ColumnsInfo columnsInfo, long maxId) {
		
		// 生成表别名
		String tableAlias = createTableAlias(table.getTableName());
		// 生成列字符串
		String columnSQL = createColumnSQL(tableAlias, columnsInfo);
		// 生成from字符串
		String fromSQL = createFromSQL(table, columnsInfo);
		// 生成where字符串
		String whereSQL = createWhereSQL(table, tableAlias, maxId);
		// 生成orderBy字符串
		String orderBySQL = createOrderBySQL(tableAlias);
		// 生成limit字符串
		String limitSQL = createLimitSQL(table);
		// String sql = "SELECT " + columnSQL + " FROM " + table.getSchemaName() + "." + table.getTableName() + " AS " + tableAlias + " WHERE " + tableAlias +".id>" + maxId + " ORDER BY " + tableAlias + ".id ASC LIMIT " + table.getLimit(); 
		String sql = "SELECT " + columnSQL + fromSQL + whereSQL + orderBySQL + limitSQL; 
		logger.info("sql：" + sql);
		return getJdbcOperations(index, true).queryForList(sql);
	}
	
	/**
	 * 生成查询列的SQL片段
	 */
	private String createColumnSQL(String tableAlias, ColumnsInfo columnsInfo) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < columnsInfo.getColumnNames().size(); i++) {
			String columnName = columnsInfo.getColumnNames().get(i);
			String joinTable = columnsInfo.getColumnJoinTable().get(i);
			String referColumn = columnsInfo.getColumnReferColumn().get(i);
			// 如果不是join列，就是自己表的列
			if(StringUtils.isBlank(joinTable)) {
				sb.append(tableAlias + "." + columnName + " AS " + columnName);
				sb.append(",");
			}else {
				// 说明是关联表的列
				String joinTableAlias = createTableAlias(joinTable);
				sb.append(joinTableAlias + "." + referColumn + " AS " + columnName);
				sb.append(",");
			}
		}
		
		/*for(String columnName : columnsInfo.getColumnNames()) {
			
			sb.append(tableAlias + "." + columnName + " AS " + columnName);
			sb.append(",");
		}*/
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private String createFromSQL(Table table, ColumnsInfo columnsInfo) {
		StringBuilder sb = new StringBuilder();
		String tableAlias = createTableAlias(table.getTableName());
		// 先加自己
		sb.append(" FROM " + table.getSchemaName() + "." + table.getTableName() + " AS " + tableAlias);
		// 寻找是否有join
		for(JoinTable joinTable : table.getJoinTables()) {
			String joinTableAlias = createTableAlias(joinTable.getName());
			sb.append(" INNER JOIN " + table.getSchemaName() + "." + joinTable.getName() + " AS " + joinTableAlias + " ON ");
			// 寻找join原始列
			for(int i = 0; i < columnsInfo.getColumnJoinTable().size(); i++) {
				String columnJoinTable = columnsInfo.getColumnJoinTable().get(i);
				if(Objects.equals(columnJoinTable, joinTable.getName())) {
					// 找到第一个匹配列就够了
					sb.append(tableAlias + "." + columnsInfo.getColumnSelfColumn().get(i) + "=" + joinTableAlias + "." + joinTable.getJoinColumn());
					break;
				}
			}
		}
		return sb.toString();
	}
	
	private String createWhereSQL(Table table, String tableAlias, long maxId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" WHERE " + tableAlias + ".id>" + maxId);
		String extraWhere = table.getExtraWhere();
		if(StringUtils.isNotBlank(extraWhere)) {
			extraWhere = extraWhere.replaceAll("#\\{alias\\}", tableAlias);
			sb.append(" AND " + extraWhere);
			
		}
		return sb.toString();
	}
	
	private String createOrderBySQL(String tableAlias) {
		StringBuilder sb = new StringBuilder();
		sb.append(" ORDER BY " + tableAlias + ".id ASC");
		return sb.toString();
	}
	
	private String createLimitSQL(Table table) {
		StringBuilder sb = new StringBuilder();
		sb.append(" LIMIT " + table.getLimit());
		return sb.toString();
	}

	/**
	 * 生成表名简称，例如provinces表会返回pr，cities表会返回ci之类的
	 */
	private String createTableAlias(String tableName) {
		return StringUtils.substring(tableName, 0, 2);
	}
	
	public int addList(int index, String schemaName, 
				String tableName, ColumnsInfo columnsInfo, 
				final List<Map<String, Object>> list){  
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		// 生成列字符串
		for(int i = 0; i < columnsInfo.getColumnNames().size(); i++) {
			String joinTable = columnsInfo.getColumnJoinTable().get(i);
			// 如果列为join列，直接跳过不参与insert
			if(StringUtils.isNotBlank(joinTable)) continue;
			String columnName = columnsInfo.getColumnNames().get(i);
			sb.append(columnName);
			sb.append(",");
			String columnType = columnsInfo.getColumnTypes().get(i);
			sb2.append(Utils.getValuePlaceHolderByColumnType(columnType));
		}
		/*for(String columnName : columnsInfo.getColumnNames()) {
			sb.append(columnName);
			sb.append(",");
		}*/
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		// 生成值占位符字符串
		/*for(String columnType : columnsInfo.getColumnTypes()) {
			sb2.append(Utils.getValuePlaceHolderByColumnType(columnType));
		}*/
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
					Boolean needInsert = columnsInfo.getNeedInsertByColumnName(columnName);
					if(!needInsert) continue;
					String columnType = columnsInfo.getTypeNameByColumnName(columnName);
					String columnConvertHandler = columnsInfo.getConvertHandlerNameByColumnName(columnName);
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
								if(StringUtils.isNotBlank(columnConvertHandler)) {
									// logger.info("columnHandler: " + columnHandler);
									ConvertHandler<String> h = (ConvertHandler<String>)Application.convertHandlersMap.get(columnConvertHandler);
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
