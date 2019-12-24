package com.github.liyibo1110.stable.elephant.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.github.liyibo1110.stable.elephant.app.Application;
import com.github.liyibo1110.stable.elephant.entity.Config;
import com.github.liyibo1110.stable.elephant.service.TransferService;

@Component
public class MySchedulingConfigurer implements SchedulingConfigurer {

	private static Logger logger = LoggerFactory.getLogger(MySchedulingConfigurer.class);
	
	@Autowired
	private TransferService transferService;
	
	private String cronTemplate = "0 minute hour * * ?";
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		
		Config config = Application.config;
		logger.info("whenHour值为：" + config.getWhenHour());
		logger.info("whenMinute值为：" + config.getWhenMinute());
		
		taskRegistrar.addTriggerTask(new Runnable() {
			@Override
			public void run() {
				transferService.transfer();
			}
			
		}, new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				return new CronTrigger(cronTemplate.replace("hour", config.getWhenHour())
												   .replace("minute", config.getWhenMinute()))
									.nextExecutionTime(triggerContext);
			}
		});

	}

}
