package com.github.liyibo1110.stable.elephant.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.liyibo1110.stable.elephant.service.TransferService;

@Controller
public class TransferController {

	private static Logger logger = LoggerFactory.getLogger(TransferController.class);

	@Autowired
	private TransferService transferService;
	
	@RequestMapping("/transfer")
	public String transfer(HttpServletRequest request, HttpServletResponse response) throws Exception{
		transferService.transfer();
		return null;
	}
	
}
