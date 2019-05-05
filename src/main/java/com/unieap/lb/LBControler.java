package com.unieap.lb;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
			HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return lbService.webappService(requestInfo, request, response, extParameters);
	}

	@RequestMapping(value = "/unieap/extAction/*")
	public String httpService(@RequestBody String requestInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return lbService.httpService(requestInfo, request, response, extParameters);
	}

	@RequestMapping(value = "/unieap/service/*")
	public String wsService(@RequestBody String requestInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return lbService.wsService(requestInfo, request, response,extParameters);
	}

	/**
	 * 一次流不能读取两次
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String charReader(HttpServletRequest request) throws Exception {
		BufferedReader br = request.getReader();
		String str, wholeStr = "";
		while ((str = br.readLine()) != null) {
			wholeStr += str;
		}
		return wholeStr;
	}

	// 二进制读取
	public String binaryReader(HttpServletRequest request) throws Exception {
		int len = request.getContentLength();
		ServletInputStream iii = request.getInputStream();
		byte[] buffer = new byte[len];
		iii.read(buffer, 0, len);
		return binaryReader(buffer);
	}

	public String binaryReader(byte[] buffer) {
		String inputData = new String(buffer, 0, buffer.length, Charset.forName("UTF-8"));
		return inputData;
	}
}
