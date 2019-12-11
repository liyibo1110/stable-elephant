# Stable-Elephant

是一个基于SpringBoot的Java应用，基础功能为可定期同步2个同构数据库的数据增量更新，通过独立的配置文件，来配置控制源库/目标库，以及要同步的表和字段（目前只支持PostgreSQL）

### 操作说明  

  1.唯一的配置文件名为config.json，需要放入/src/main/resources目录中，格式为：
  ```
  {
	"databasePairs": [
		{
			"name": "数据库对名称",
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
					"columns": [
						{ "name": "id", "type": "int4" },
						{ "name": "name", "type": "varchar" },
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
					"columns": [
						{ "name": "id", "type": "int4" },
						{ "name": "name", "type": "varchar" },
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
  
