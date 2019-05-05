package com.unieap.base;

import java.util.HashMap;
import java.util.Map;

public class UnieapCacheMgt {
	public static Map<String, String> appBizList = new HashMap<String, String>();
	public static Map<String, String> appSOAPList = new HashMap<String, String>();
	public static Map<String, String> getAppBizList() {
		return appBizList;
	}
	public static void setAppBizList(Map<String, String> appBizList) {
		UnieapCacheMgt.appBizList = appBizList;
	}
	public static Map<String, String> getAppSOAPList() {
		return appSOAPList;
	}
	public static void setAppSOAPList(Map<String, String> appSOAPList) {
		UnieapCacheMgt.appSOAPList = appSOAPList;
	}

	
}
