package com.unieap.lb;

import java.io.BufferedReader;
import java.io.IOException;

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
	// unieap bizservice
	public String wsServicePrfix = "/unieap/service/BizService";
	// unieap-esb-ws
	public String wsCustomizeServicePrfix = "/unieap/service/";

	public String webappService(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURL().toString();
		if (url.indexOf(httpServicePrfix) > 0) {
			return httpService(request, response);
		} else {
			return restTemplate.postForObject(
					"http://UNIEAP-ESB-WEB"
							+ url.substring(url.indexOf(webappServicePrfix) + webappServicePrfix.length()),
					request, String.class);
		}
	}

	public String httpService(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURL().toString();
		if (url.indexOf(httpServicePrfix) > 0) {
			return restTemplate.postForObject(
					"http://UNIEAP-ESB" + url.substring(url.indexOf(httpServicePrfix) + httpServicePrfix.length()),
					request, String.class);
		} else {
			return restTemplate.getForObject(url, String.class);
		}
	}

	public String wsService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestString = ReadAsChars(request);
		String url = request.getRequestURL().toString();
		url = getUrlPrefix("UNIEAP-ESB") + url.substring(url.indexOf(wsServicePrfix));
		return callws(url, requestString);
	}

	public String wsCuzService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestString = ReadAsChars(request);
		String url = request.getRequestURL().toString();
		url = getUrlPrefix("UNIEAP-ESB-WS") + url.substring(url.indexOf(wsCustomizeServicePrfix));
		return callws(url, requestString);
	}

	private String callws(String url, String requestString) throws Exception {
		InfConfigVO infConfigVO = new InfConfigVO();
		infConfigVO.setUrl(url);
		infConfigVO.setTimeout(6000);
		return SoapCallUtils.callHTTPService(infConfigVO, requestString);
	}

	private String getUrlPrefix(String appName) {
		ServiceInstance serviceInstance = this.loadBalancerClient.choose(appName);
		// String serviceId =serviceInstance.getServiceId();
		//String host = serviceInstance.getHost();
		//int port = serviceInstance.getPort();
		return serviceInstance.getUri().toString();
	}

	private String ReadAsChars(HttpServletRequest request) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder("");
		try {
			br = request.getReader();
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}
