package com.unieap.lb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LBControler {

	@Autowired
	LBService lbService;
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping(value = "/unieap-web/*")
	public String webappService(HttpServletRequest request, HttpServletResponse response) {
		return lbService.webappService(request, response);
	}
	@RequestMapping(value = "/unieap/extAction/*")
	public String httpService(HttpServletRequest request, HttpServletResponse response) {
		return lbService.httpService(request, response);
	}

	@RequestMapping(value = "/unieap/service/*")
	public String wsService(HttpServletRequest request, HttpServletResponse response) {
		try {
			return lbService.wsService(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
	}
	@RequestMapping(value = "/biz/service/*")
	public String wsCuzService(HttpServletRequest request, HttpServletResponse response) {
		try {
			return lbService.wsCuzService(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "failed";
	}
}
