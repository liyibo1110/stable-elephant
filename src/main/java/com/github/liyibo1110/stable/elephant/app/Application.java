package com.github.liyibo1110.stable.elephant.app;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.fastjson.JSONObject;
import com.github.liyibo1110.stable.elephant.entity.Config;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages={"com.github.liyibo1110.stable.elephant"})
@ConfigurationPropertiesScan	// 2.2.1新增，必须要加
public class Application {

	public static Config config;
	
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
			// System.out.println(config);
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
