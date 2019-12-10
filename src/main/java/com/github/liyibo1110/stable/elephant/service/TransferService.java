package com.github.liyibo1110.stable.elephant.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.stereotype.Service;

import com.github.liyibo1110.stable.elephant.app.Application;
import com.github.liyibo1110.stable.elephant.component.DatabaseHolder;
import com.github.liyibo1110.stable.elephant.dao.DynamicDao;
import com.github.liyibo1110.stable.elephant.entity.Column;
import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.entity.DatabasePair;
import com.github.liyibo1110.stable.elephant.entity.Table;
import com.github.liyibo1110.stable.elephant.util.Utils;

@Service
public class TransferService {

	private static Logger logger = LoggerFactory.getLogger(TransferService.class);

	@Autowired
	private DatabaseHolder databaseHolder;
	
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
		logger.info(config.toString());
		for(int i = 0; i < config.getDatabasePairs().size(); i++) {
			DatabasePair dbPair = config.getDatabasePairs().get(i);
			transfer(i, dbPair);
		}
	}
	
	private void transfer(int pairsIndex, DatabasePair dbPair) {
		logger.info("开始处理" + dbPair.getName());
		for(Table table : dbPair.getTables()) {
			transfer(pairsIndex, table);
		}
	}
	
	private void transfer(int pairsIndex, Table table) {
		logger.info("开始处理" + table.getTableName());
		ColumnsInfo columnsInfo = createColumnsInfo(table.getColumns());
		Object dynamicObject = generateObjectByField(columnsInfo);
		Long maxId = dynamicDao.getCountTableMaxId(pairsIndex, table.getSchemaName(), table.getTableName());
		logger.info("maxId：" + maxId);
		
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

	private ColumnsInfo createColumnsInfo(List<Column> columns) {
		ColumnsInfo info = new ColumnsInfo();
		for(Column c : columns) {
			info.addColumnName(c.getName());
			info.addColumnType(c.getType());
		}
		return info;
	}
	
	private Object generateObjectByField(ColumnsInfo info) {
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
		/*try {
			BeanUtils.setProperty(obj, "id", 123);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}*/
		return obj;
	}
}
