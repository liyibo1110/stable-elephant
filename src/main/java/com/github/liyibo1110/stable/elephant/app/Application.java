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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.alibaba.fastjson.JSONObject;
import com.github.liyibo1110.stable.elephant.entity.AfterQueryHandler;
import com.github.liyibo1110.stable.elephant.entity.ColumnConvertHandler;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.handler.AfterHandler;
import com.github.liyibo1110.stable.elephant.handler.ConvertHandler;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages={"com.github.liyibo1110.stable.elephant"})
@ConfigurationPropertiesScan	// 2.2.1新增，必须要加
@EnableScheduling
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static Config config;
	/**
	 * 存储列columnConvertHandler的name和handler对象
	 */
	public static Map<String, ConvertHandler<?, ?>> convertHandlersMap = new HashMap<>();
	
	public static Map<String, AfterHandler> afterHandlersMap = new HashMap<>();
	
	public static void main(String[] args) {
		logger.info("进入了main");
		ApplicationContext context = SpringApplication.run(Application.class, args);
		SpringContextUtil.setApplicationContext(context);
		logger.info("main完事了");
	}

	@PostConstruct
	public void start() {
		logger.info("进入了start");
		ClassPathResource classPathResource = new ClassPathResource("config.json");
        try {
			String str = IOUtils.toString(new InputStreamReader(classPathResource.getInputStream(),"UTF-8"));
			// System.out.println(str);
			config = JSONObject.parseObject(str, Config.class);
			parseConvertHandlersMap(config);
			parseAfterHandlersMap(config);
			// System.out.println(config);
			// logger.info("app初始化完成");
			// logger.info(handlersMap.toString());
			
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        logger.info("start完事了");
	}
	
	private void parseConvertHandlersMap(Config config) {
		List<ColumnConvertHandler> handlers = config.getColumnConvertHandlers();
		for(ColumnConvertHandler handler : handlers) {
			// 实例化handler里面的类
			Class<?> clazz = null;
			try {
				 clazz = Class.forName(handler.getHandler());
				 ConvertHandler<?, ?> h = (ConvertHandler<?, ?>)ConstructorUtils.invokeConstructor(clazz);
				 convertHandlersMap.put(handler.getName(), h);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parseAfterHandlersMap(Config config) {
		List<AfterQueryHandler> handlers = config.getAfterQueryHandlers();
		for(AfterQueryHandler handler : handlers) {
			// 实例化handler里面的类
			Class<?> clazz = null;
			try {
				 clazz = Class.forName(handler.getHandler());
				 logger.info("handler:" + handler.getHandler());
				 AfterHandler h = (AfterHandler)ConstructorUtils.invokeConstructor(clazz);
				 afterHandlersMap.put(handler.getName(), h);
				 logger.info("name:" + handler.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			/*logger.info("进入了parseAfterHandlersMap");
			// TransferService ts = (TransferService)SpringContextUtil.getBean(TransferService.class);
			TransferService ts = (TransferService)SpringContextUtil.getBean("transferService");
			
			if(ts == null) {
				logger.info("ts为null");
			}else {
				logger.info("ts不为null");
			}
			
			String name = handler.getName();
			logger.info("name:" + name);
			AfterHandler h = (AfterHandler)SpringContextUtil.getBean(handler.getName());
			afterHandlersMap.put(handler.getName(), h);*/
		}
	}
}
