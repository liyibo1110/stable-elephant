package com.github.liyibo1110.stable.elephant.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CityNameColumnHandler implements ConvertHandler<String, Integer> {

	private static Logger logger = LoggerFactory.getLogger(CityNameColumnHandler.class);
	
	@Override
	public Integer handler(String value) {
		// logger.info("要处理的值为：" + value);
		// return value + "456";
		return value.hashCode();
	}

}
