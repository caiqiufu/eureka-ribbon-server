package com.unieap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.lb.LBService;

@SpringBootApplication
@EnableDiscoveryClient
public class EurekaRibbonServerApplication {
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(EurekaRibbonServerApplication.class, args);
		LBService lBService = (LBService) ApplicationContextProvider.getBean("LBService");
		lBService.loadAppList();
	}
}
