package com.github.liyibo1110.stable.elephant.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.fastjson.JSONObject;
import com.github.liyibo1110.stable.elephant.entity.ColumnHandler;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.handler.Handler;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages={"com.github.liyibo1110.stable.elephant"})
@ConfigurationPropertiesScan	// 2.2.1新增，必须要加
@EnableScheduling
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static Config config;
	/**
	 * 存储列columnHandler的name和handler对象
	 */
	public static Map<String, Handler<?>> handlersMap = new HashMap<>();
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void start() {
		ClassPathResource classPathResource = new ClassPathResource("config.json");
        try {
			String str = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(),"UTF-8"));
			// System.out.println(str);
			config = JSONObject.parseObject(str, Config.class);
			parseHandlersMap(config);
			// System.out.println(config);
			// logger.info("app初始化完成");
			// logger.info(handlersMap.toString());
			
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseHandlersMap(Config config) {
		List<ColumnHandler> handlers = config.getColumnHandlers();
		for(ColumnHandler handler : handlers) {
			// 实例化handler里面的类
			Class<?> clazz = null;
			try {
				 clazz = Class.forName(handler.getHandler());
				 Handler<?> h = (Handler<?>)ConstructorUtils.invokeConstructor(clazz);
				 handlersMap.put(handler.getName(), h);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
