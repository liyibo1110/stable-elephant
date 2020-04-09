# Stable-Elephant

是一个基于SpringBoot的Java应用，基础功能为可定期同步2个同构数据库的数据增量更新，通过独立的配置文件，来配置控制源库/目标库，以及要同步的表和字段（目前只支持PostgreSQL）

## 版本更新
```
2020-04-09 v0.3.1
1.table配置项中增加了extraWhere的配置，用来人工写一些额外的where过滤条件以满足查询原始数据的特殊范围。（注意列前面要增加#{alias}这样的固定标识，辅助最终生成完整的SQL片段，因为这里的值可以任意写复杂，所以无法自动增加表别名）

2020-04-08 v0.3.0
1.增加了表连接INNER JOIN功能，在获取原始数据时可以通过配置来实现跨表查询。（通过表连接而来的它表字段，是不会被写入目标数据库的，一般用来配合自定义处理行为这个功能使用）
2.增加获取原始数据后自定义处理行为（例如可以将数据保存到本地，或者通过http推送到其他系统接口），同时会取代原来默认的写入目标数据库表的行为。

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
	"columnConvertHandlers": [
		{
			"name": "provinceNameColumnHandler",
			"handler": "com.github.liyibo1110.stable.elephant.handler.ProvinceNameColumnHandler"
		},
		{
			"name": "cityNameColumnHandler",
			"handler": "com.github.liyibo1110.stable.elephant.handler.CityNameColumnHandler"
		}
	],
	"afterQueryHandlers": [
		{
			"name": "CityAfterQueryHandler",
			"handler": "com.github.liyibo1110.stable.elephant.handler.CityAfterQueryHandler"
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
						{ "name": "name", "type": "varchar", "convertHandler": "provinceNameColumnHandler" },
						{ "name": "enabled", "type": "bool" },
						{ "name": "add_time", "type": "timestamp" },
						{ "name": "update_time", "type": "timestamp" }
					]
				},
				{
					"schemaName": "public",
					"tableName": "table2",
					"afterQueryHandler": "CityAfterQueryHandler",
					"joinTables": [
						{
							"name": "provinces",
							"joinColumn": "id"
						}
					],
					"countTableName": "table2_count",
					"limit": 100,
					"enabled": true,
					"extraWhere": "#{alias}.add_time<'2019-01-01'::timestamp",
					"columns": [
						{ "name": "id", "type": "int4" },
						{ "name": "name", "type": "varchar", "convertHandler": "cityNameColumnHandler" },
						{ "name": "spell", "type": "varchar" },
						{ "name": "lat", "type": "numeric" },
						{ "name": "lng", "type": "numeric" },
						{ "name": "add_time", "type": "timestamp" },
						{ "name": "update_time", "type": "timestamp" },
						{ "name": "province_name", "type": "varchar", "joinTable": "provinces", "selfColumn": "province_id", "referColumn": "name"}
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
  
