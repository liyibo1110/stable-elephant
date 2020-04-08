package com.github.liyibo1110.stable.elephant.handler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.github.liyibo1110.stable.elephant.entity.ColumnsInfo;

public class CityAfterQueryHandler implements AfterHandler {

	private static Logger logger = LoggerFactory.getLogger(CityAfterQueryHandler.class);
	
	@Override
	public boolean handler(List<Map<String, Object>> datas, ColumnsInfo columnsInfo) {
		logger.info(datas.toString());
		try {
			FileUtils.writeStringToFile(new File("d:\\result.txt"), JSON.toJSONString(datas));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}
