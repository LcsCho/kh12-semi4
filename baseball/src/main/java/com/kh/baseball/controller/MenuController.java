package com.kh.baseball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MenuController {

	@RequestMapping("/schedule/doosan")
	public String scheduleDoosan() {
		return "/WEB-INF/views/menu/scheduleDoosan.jsp";
	}	
	
	@RequestMapping("/schedule/lg")
	public String scheduleLg() {
		return "/WEB-INF/views/menu/scheduleLg.jsp";
	}	
}
