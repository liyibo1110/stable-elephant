package com.github.liyibo1110.stable.elephant.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvinceNameColumnHandler implements ConvertHandler<String> {

	private static Logger logger = LoggerFactory.getLogger(ProvinceNameColumnHandler.class);
	
	@Override
	public String handler(String value) {
		// logger.info("要处理的值为：" + value);
		return value + "123";
	}

}
