package com.github.liyibo1110.stable.elephant.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping("/apilist")
	public String apiList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return "apiList";
	}
}
