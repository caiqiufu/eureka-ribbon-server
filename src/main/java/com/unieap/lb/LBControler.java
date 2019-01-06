package com.unieap.lb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
	public String webappService(@RequestBody String requestInfo, HttpServletRequest request,
			HttpServletResponse response) {
		return lbService.webappService(requestInfo, request, response);
	}

	@RequestMapping(value = "/unieap/extAction/*")
	public String httpService(@RequestBody String requestInfo, HttpServletRequest request,
			HttpServletResponse response) {
		return lbService.httpService(requestInfo, request, response);
	}

	@RequestMapping(value = "/unieap/service/*")
	public String wsService(@RequestBody String requestInfo, HttpServletRequest request, HttpServletResponse response) {
		try {
			return lbService.wsService(requestInfo, request, response);
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
