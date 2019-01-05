package com.unieap.base.vo;

public class InfConfigVO {
	private java.lang.Integer id;
	private String bizCode;
	private String appName;
	private String infName;
	private String url;
	private String className;
	private String activeFlag;
	private String accessName;
	private String password;
	private String errorCodeIgnore;
	private String successCode;
	private byte[] responseSample;
	private java.lang.Integer timeout;
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public String getInfName() {
		return infName;
	}
	public void setInfName(String infName) {
		this.infName = infName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public java.lang.Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(java.lang.Integer timeout) {
		this.timeout = timeout;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getErrorCodeIgnore() {
		return errorCodeIgnore;
	}
	public void setErrorCodeIgnore(String errorCodeIgnore) {
		this.errorCodeIgnore = errorCodeIgnore;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	public byte[] getResponseSample() {
		return responseSample;
	}
	public void setResponseSample(byte[] responseSample) {
		this.responseSample = responseSample;
	}
	public String getAccessName() {
		return accessName;
	}
	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
