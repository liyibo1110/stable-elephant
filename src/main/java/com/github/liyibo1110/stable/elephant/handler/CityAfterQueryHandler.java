package com.github.liyibo1110.stable.elephant.handler;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.liyibo1110.stable.elephant.app.SpringContextUtil;
import com.github.liyibo1110.stable.elephant.component.HttpFetcher;
import com.github.liyibo1110.stable.elephant.dto.TravelMLRequest;
import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;
import com.github.liyibo1110.stable.elephant.util.Constants;

// @Component("cityAfterQueryHandler")
// @Component
public class CityAfterQueryHandler implements AfterHandler {

	private static Logger logger = LoggerFactory.getLogger(CityAfterQueryHandler.class);
	
	/*@Autowired
	private HttpFetcher fetcher;*/
	
	@Override
	public boolean handler(List<Map<String, Object>> datas, ColumnsInfo columnsInfo) {
		
		// 人工转换一些时间值
		for(Map<String, Object> map : datas) {
			Timestamp addTime = (Timestamp)map.get("add_time");
			map.put("add_time", LocalDateTime.ofInstant(Instant.ofEpochMilli(addTime.getTime()), ZoneId.systemDefault()).toString());
		
			Timestamp updateTime = (Timestamp)map.get("update_time");
			map.put("update_time", LocalDateTime.ofInstant(Instant.ofEpochMilli(updateTime.getTime()), ZoneId.systemDefault()).toString());
		}
		
		// logger.info(datas.toString());
		
		String message = JSONObject.toJSONString(new TravelMLRequest("Cities", "insert", datas));
		
		HttpFetcher fetcher = (HttpFetcher)SpringContextUtil.getBean(HttpFetcher.class);
		if(fetcher == null) {
			logger.info("fetcher为null");
		}
		// logger.info("请求json内容为：" + message);
		String result = fetcher.post(Constants.IMPORT_DATA_REQUEST_URL, message);
		logger.info("对方响应json内容为：" + result);
		
		
		try {
			String filename = "result" + System.currentTimeMillis() + ".txt";
			FileUtils.writeStringToFile(new File("d:\\" + filename), message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
