# Stable-Elephant

是一个基于SpringBoot的Java应用，基础功能为可定期同步2个同构数据库的数据增量更新，通过独立的配置文件，来配置控制源库/目标库，以及要同步的表和字段（目前只支持PostgreSQL）

## 版本更新
```
2019-12-24 v0.2.0
1. 增加了对数据值进行迁移转换的Handler机制，类似Mybatis，可在config文件中配置handler类，然后配置到columns中的特定列，代码中新增实现了Handler接口的handler方法的类进行注册即可。
2. 增加了简单定期执行配置（whenHour、whenMinute），到时会自动执行transfer。
3. 增加了2个级别的enabled开关，用来控制数据库以及表是否需要迁移。
```


### 操作说明  

  1.唯一的配置文件名为config.json，需要放入/src/main/resources目录中，格式为：
  ```
  {
  	"whenHour": "2",
	"whenMinute": "30",
	"columnHandlers": [
		{
			"name": "provinceNameColumnHandler",
			"handler": "com.github.liyibo1110.stable.elephant.handler.ProvinceNameColumnHandler"
		},
		{
			"name": "cityNameColumnHandler",
			"handler": "com.github.liyibo1110.stable.elephant.handler.CityNameColumnHandler"
		}
	],
	"databasePairs": [
		{
			"name": "数据库对名称",
			"enabled": true,
			"sourceDatabase": {
				"host": "192.168.1.111",
				"port": "15432",
				"username": "postgres",
				"password": "******",
				"database": "db1"
			},
			"targetDatabase": {
				"host": "192.168.1.222",
				"port": "5432",
				"username": "postgres",
				"password": "******",
				"database": "db1_target"
			},
			"tables": [
			{
					"schemaName": "public",
					"tableName": "table1",
					"countTableName": "table1_count",
					"limit": 100,
					"enabled": true,
					"columns": [
						{ "name": "id", "type": "int4" },
						{ "name": "name", "type": "varchar", "handler": "provinceNameColumnHandler" },
						{ "name": "enabled", "type": "bool" },
						{ "name": "add_time", "type": "timestamp" },
						{ "name": "update_time", "type": "timestamp" }
					]
				},
				{
					"schemaName": "public",
					"tableName": "table2",
					"countTableName": "table2_count",
					"limit": 100,
					"enabled": true,
					"columns": [
						{ "name": "id", "type": "int4" },
						{ "name": "name", "type": "varchar", "handler": "cityNameColumnHandler" },
						{ "name": "spell", "type": "varchar" },
						{ "name": "lat", "type": "numeric" },
						{ "name": "lng", "type": "numeric" },
						{ "name": "add_time", "type": "timestamp" },
						{ "name": "update_time", "type": "timestamp" }
					]
				},
				{
					"schemaName": "public",
					"tableName": "table3",
					"countTableName": "table3_count",
					"limit": 1000,
					"enabled": false,
					"columns": [
						{ "name": "id", "type": "int8" },
						{ "name": "type_ids", "type": "int4[]" },
						{ "name": "image_urls", "type": "jsonb" },
						{ "name": "enabled", "type": "bool" },
						{ "name": "add_time", "type": "timestamp" },
						{ "name": "update_time", "type": "timestamp" }
					]
				}
			]
		}
	]
}
```
  2.回头补上定期任务这块的功能 
  
