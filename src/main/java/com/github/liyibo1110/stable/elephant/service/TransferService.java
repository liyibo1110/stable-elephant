package com.github.liyibo1110.stable.elephant.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.liyibo1110.stable.elephant.app.Application;
import com.github.liyibo1110.stable.elephant.dao.DynamicDao;
import com.github.liyibo1110.stable.elephant.entity.Column;
import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.entity.DatabasePair;
import com.github.liyibo1110.stable.elephant.entity.Table;
import com.github.liyibo1110.stable.elephant.handler.AfterHandler;

@Service
public class TransferService {

	private static Logger logger = LoggerFactory.getLogger(TransferService.class);

	/*@Autowired
	private DatabaseHolder databaseHolder;*/
	
	@Autowired
	private DynamicDao dynamicDao;
	
	public void transfer() {
		
		/*List<List<JdbcOperations>> list = databaseHolder.getOperationsList();
		logger.info("获取到的list大小为：" + list.size());
		for(List<JdbcOperations> templates : list) {
			for(JdbcOperations template : templates) {
				logger.info(template.toString());
			}
		}*/
		
		Config config = Application.config;
		// logger.info(config.toString());
		for(int i = 0; i < config.getDatabasePairs().size(); i++) {
			DatabasePair dbPair = config.getDatabasePairs().get(i);
			if(dbPair.getEnabled()) {
				transfer(i, dbPair);
			}
		}
	}
	
	private void transfer(int pairsIndex, DatabasePair dbPair) {
		logger.info("开始处理" + dbPair.getName());
		for(Table table : dbPair.getTables()) {
			if(table.getEnabled()) {
				transfer(pairsIndex, table);
			}
		}
	}
	
	private void transfer(int pairsIndex, Table table) {
		logger.info("开始处理" + table.getTableName());
		ColumnsInfo columnsInfo = createColumnsInfo(table.getColumns());
		// Object dynamicObject = generateObjectByField(columnsInfo);
		
		while(true) {
			// 获取单表的maxId
			Long maxId = dynamicDao.getCountTableMaxId(pairsIndex, table.getSchemaName(), table.getCountTableName());
			logger.info("maxId：" + maxId);
			// List<Map<String, Object>> resultList = dynamicDao.getList(pairsIndex, table.getSchemaName(), table.getTableName(), columnsInfo, maxId, table.getLimit());
			List<Map<String, Object>> resultList = dynamicDao.getList(pairsIndex, table, columnsInfo, maxId);
			logger.info("查询完成，共" + resultList.size() + "条数据");
			
			break;
			
			// logger.info(resultList.toString());
			/*int size = resultList.size();
			if(size == 0) {
				logger.info(table.getTableName() + "表已没有数据要同步，同步完成");
				break;
			}
			
			// 重点，检查是否注册了afterQueryHandler
			String afterQueryHandlerName = table.getAfterQueryHandler();
			if(StringUtils.isBlank(afterQueryHandlerName)) {
				dynamicDao.addList(pairsIndex, table.getSchemaName(), table.getTableName(), columnsInfo, resultList);
				logger.info("批量insert已成功");
			}else {
				// 如果注册了，则不执行addList写数据库操作，改为执行自定义的handler方法
				AfterHandler h = (AfterHandler)Application.afterHandlersMap.get(afterQueryHandlerName);
				boolean result = h.handler(resultList, columnsInfo);
				logger.info("afterHandler处理结果为：" + result);
			}
			// 回写maxId
			maxId = Long.parseLong(resultList.get(resultList.size() - 1).get("id").toString());
			logger.info("准备回写最新maxId：" + maxId);
			dynamicDao.updateCountTableMaxId(pairsIndex, table.getSchemaName(), table.getCountTableName(), maxId);
			logger.info("回写maxId成功");
			// 判断是否还要循环（size位于0和limit之间，绝对不等于0，最大是limit）
			if(size < table.getLimit()) {
				logger.info(table.getTableName() + "表已同步到最后一批数据，同步完成");
				break;
			}*/
			
			
			// logger.info(columnsInfo.toString());
			// logger.info(dynamicObject.toString());
			
			
			/*String v = null;
			try {
				v = BeanUtils.getProperty(dynamicObject, "id");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			logger.info("id：" + v);*/
		}
		
		
	}

	private ColumnsInfo createColumnsInfo(List<Column> columns) {
		ColumnsInfo info = new ColumnsInfo();
		for(Column c : columns) {
			info.addColumnName(c.getName());
			info.addColumnType(c.getType());
			info.addColumnConvertHandlers(c.getConvertHandler());
			info.addColumnJoinTable(c.getJoinTable());
			info.addColumnSelfColumn(c.getSelfColumn());
			info.addColumnReferColumn(c.getReferColumn());
			
			info.putColumnNameAndColumnType(c.getName(), c.getType());
			info.putColumnNameAndColumnConvertHandler(c.getName(), c.getConvertHandler());
			if(StringUtils.isBlank(c.getJoinTable())) {
				info.putColumnNameAndNeedInsertHandler(c.getName(), true);
			}else {
				info.putColumnNameAndNeedInsertHandler(c.getName(), false);
			}
		}
		return info;
	}
	
	/*private Object generateObjectByField(ColumnsInfo info) {
		BeanGenerator generator = new BeanGenerator();
		// 增加动态属性字段
		for(int i = 0; i < info.getColumnNames().size(); i++) {
			String columnName = info.getColumnNames().get(i);
			String columnType = info.getColumnTypes().get(i);
			//logger.info("type：" + columnType);
			generator.addProperty(columnName, Utils.getClassByTypeName(columnType));
		}
		// 增加固定字段
		generator.addProperty("values", Object[].class);
		generator.addProperty("valueTypes", Integer[].class);
		
		Object obj = generator.create();
		// 给字段赋值
		try {
			BeanUtils.setProperty(obj, "id", 123);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}*/
}
