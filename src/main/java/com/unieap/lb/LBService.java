package com.unieap.lb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.unieap.base.vo.InfConfigVO;
import com.unieap.service.esb.inf.unitls.SoapCallUtils;

@Service
public class LBService {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	public String webappServicePrfix = "/unieap-web/";
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
	public String webappService(String requestInfo, HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURL().toString();
		return restTemplate.postForObject("http://UNIEAP-ESB-WEB" + url.substring(url.indexOf(webappServicePrfix)),
				requestInfo, String.class);
	}
	/**
	 * 
	 * @param requestInfo
	 * @param request
	 * @param response
	 * @return
	 */
	public String httpService(String requestInfo, HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURL().toString();
		if (url.indexOf(httpServicePrfix) > 0) {
			return restTemplate.postForObject("http://UNIEAP-ESB" + url.substring(url.indexOf(httpServicePrfix)),
					requestInfo, String.class);
		} else {
			return restTemplate.getForObject(url, String.class);
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
	public String wsService(String requestInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String url = request.getRequestURL().toString();
		url = getUrlPrefix("UNIEAP-ESB-WS") + url.substring(url.indexOf(wsServicePrfix));
		return callws(url, requestInfo);
	}

	private String callws(String url, String requestString) throws Exception {
		InfConfigVO infConfigVO = new InfConfigVO();
		infConfigVO.setUrl(url);
		infConfigVO.setTimeout(1000);
		return SoapCallUtils.callHTTPService(infConfigVO, requestString);
	}

	private String getUrlPrefix(String appName) throws Exception {
		ServiceInstance serviceInstance = this.loadBalancerClient.choose(appName);
		// String serviceId =serviceInstance.getServiceId();
		// String host = serviceInstance.getHost();
		// int port = serviceInstance.getPort();
		if(serviceInstance ==null) {
			throw new Exception("not find serviceInstance from server["+appName+"]");
		}
		return serviceInstance.getUri().toString();
	}
}
