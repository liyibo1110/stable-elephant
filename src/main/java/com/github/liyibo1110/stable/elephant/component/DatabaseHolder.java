package com.github.liyibo1110.stable.elephant.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.liyibo1110.stable.elephant.app.Application;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.entity.Database;
import com.github.liyibo1110.stable.elephant.entity.DatabasePair;

@Component
public class DatabaseHolder {

	private static Logger logger = LoggerFactory.getLogger(DatabaseHolder.class);
	
	public static final String PG_DRIVER_NAME = "org.postgresql.Driver";
	
	private List<List<JdbcOperations>> jdbcOperationsList = new ArrayList<>();
	
	@PostConstruct
	private void run() {
		
		Config config = Application.config;
		List<DatabasePair> dbPairs = config.getDatabasePairs();
		if (dbPairs.size() == 0) {
			throw new IllegalStateException("应用异常，databasePairs列表没有数据！");
		}
		logger.info("共有" + dbPairs.size() + "条用户对库的映射信息");
		for(DatabasePair dbPair : dbPairs) {
			loadDatabase(dbPair);
		}
	}
	
	public void loadDatabase(DatabasePair dbPair) {
		// logger.info("开始动态创建template");
		List<JdbcOperations> list = new ArrayList<>();
		
		// 先搞source库
		Database sourceDb = dbPair.getSourceDatabase();
		// 构建DataSrouce
		DruidDataSource sourceDataSource = buildDruidDataSource(sourceDb);
		// 构建JdbcOperations
		JdbcOperations sourceTemplate = buildJdbcOperations(sourceDataSource);
		// 加入list
		list.add(sourceTemplate);
		
		// 再搞target库
		Database targetDb = dbPair.getTargetDatabase();
		// 构建DataSrouce
		DruidDataSource targetDataSource = buildDruidDataSource(targetDb);
		// 构建JdbcOperations
		JdbcOperations targetTemplate = buildJdbcOperations(targetDataSource);
		// 加入list
		list.add(targetTemplate);
		
		// 加入总list
		jdbcOperationsList.add(list);
		
		logger.info("创建jdbcOperationsList完成，共有" + jdbcOperationsList.size());
	}
	
	@Bean(name="JdbcOperationsList")
	public List<List<JdbcOperations>> getOperationsList() {
		return jdbcOperationsList;
	}
	
	private DruidDataSource buildDruidDataSource(Database db) {
		String url = "jdbc:postgresql://" + db.getHost() + ":" + db.getPort() + "/" + db.getDatabase() + "?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF8";
		DruidDataSource ds = DataSourceBuilder.create()
						 					  .type(com.alibaba.druid.pool.DruidDataSource.class)
						 					  .driverClassName(PG_DRIVER_NAME)
						 					  .url(url)
						 					  .username(db.getUsername())
						 					  .password(db.getPassword())
						 					  .build();
		ds.setInitialSize(10);
		ds.setMinIdle(5);
		ds.setMaxActive(50);
		
		ds.setMaxWait(60000);
		ds.setTimeBetweenEvictionRunsMillis(60000);
		ds.setMinEvictableIdleTimeMillis(300000);
		ds.setValidationQuery("SELECT 1");
		ds.setTestWhileIdle(true);
		ds.setTestOnBorrow(false);
		ds.setTestOnReturn(false);
		
		ds.setPoolPreparedStatements(false);
		ds.setMaxPoolPreparedStatementPerConnectionSize(0);
		return ds;
	}
	
	private JdbcOperations buildJdbcOperations(DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	
}
