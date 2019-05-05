package com.unieap.lb;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.unieap.base.UnieapCacheMgt;

import net.sf.json.JSONObject;

@Service
public class LBService {
	public Logger logger = Logger.getLogger(LBService.class);

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	JdbcTemplate jdbcTemplate;
	public String webappServicePrfix = "/unieap-web/";
	// unieap-esb
	public String httpServicePrfix = "/unieap/extAction/";
	// unieap-esb-ws
	public String wsServicePrfix = "/unieap/service/";

	/**
	 * 
	 * @param requestInfo
	 * @param request
	 * @param response
	 * @return
	 */
	public String webappService(String requestInfo, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> extParameters) {
		String url = request.getRequestURL().toString();
		HttpHeaders requestHeaders = copyHttpHeaders(request);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestInfo, requestHeaders);
		url = "http://UNIEAP-ESB-WEB" + url.substring(url.indexOf(webappServicePrfix));
		return restTemplate.postForObject(url, requestEntity, String.class);
	}

	/**
	 * 
	 * @param requestInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String httpService(String requestInfo, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> extParameters) throws Exception {
		String url = request.getRequestURL().toString();
		HttpHeaders requestHeaders = copyHttpHeaders(request);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestInfo, requestHeaders);
		JSONObject jsonResult = JSONObject.fromObject(requestInfo);
		String bizCode = jsonResult.getJSONObject("requestBody").getString("bizCode");
		if ("E0001".equals(bizCode)) {
			this.loadAppList();
			return "success";
		}
		String appName = UnieapCacheMgt.getAppBizList().get(bizCode);
		if (StringUtils.isEmpty(appName)) {
			return restTemplate.postForObject(url, requestEntity, String.class);
		} else {
			url = "http://" + appName.toUpperCase() + url.substring(url.indexOf(httpServicePrfix));
			return restTemplate.postForObject(url, requestEntity, String.class);
		}
	}

	/**
	 * 
	 * @param requestInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String wsService(String requestInfo, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> extParameters) throws Exception {
		String url = request.getRequestURL().toString();
		String SOAPAction = request.getHeader("SOAPAction");
		SOAPAction = SOAPAction.substring(1, SOAPAction.length() - 1);
		String appName = UnieapCacheMgt.getAppSOAPList().get(SOAPAction);
		HttpHeaders requestHeaders = copyHttpHeaders(request);
		HttpEntity<String> requestEntity = new HttpEntity<String>(requestInfo, requestHeaders);
		if (StringUtils.isEmpty(appName)) {
			return restTemplate.postForObject(url, requestEntity, String.class);
		} else {
			url = "http://" + appName.toUpperCase() + url.substring(url.indexOf(wsServicePrfix));
			return restTemplate.postForObject(url, requestEntity, String.class);
		}
	}

	public HttpHeaders copyHttpHeaders(HttpServletRequest request) {
		HttpHeaders requestHeaders = new HttpHeaders();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			requestHeaders.add(key, value);
		}
		return requestHeaders;
	}

	public void loadAppList() {
		String sqlBiz = "SELECT biz_code,app_name FROM unieap.esb_biz_config group by biz_code,app_name";
		List<Map<String, Object>> datasBiz = jdbcTemplate.queryForList(sqlBiz);
		for (Map<String, Object> data : datasBiz) {
			String bizCode = (String) data.get("biz_code");
			String appName = (String) data.get("app_name");
			UnieapCacheMgt.getAppBizList().put(bizCode, appName.toUpperCase());
		}
		String sqlSOAP = "SELECT SOAP_Action,app_name FROM unieap.esb_biz_config where SOAP_Action is not null group by SOAP_Action,app_name";
		List<Map<String, Object>> datasSOAP = jdbcTemplate.queryForList(sqlSOAP);
		for (Map<String, Object> data : datasSOAP) {
			String bizCode = (String) data.get("SOAP_Action");
			String appName = (String) data.get("app_name");
			UnieapCacheMgt.getAppSOAPList().put(bizCode, appName.toUpperCase());
		}
	}
}
