package com.unieap.base.vo;

public class NumberFilterVO {
	private Long Id;
	private String infCode;
	private String numberStart;
	private String numberEnd;
	private byte[] responseSample;
	private String extParameters;
	private String activateFlag;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}


	public String getInfCode() {
		return infCode;
	}

	public void setInfCode(String infCode) {
		this.infCode = infCode;
	}

	public String getNumberStart() {
		return numberStart;
	}

	public void setNumberStart(String numberStart) {
		this.numberStart = numberStart;
	}

	public String getNumberEnd() {
		return numberEnd;
	}

	public void setNumberEnd(String numberEnd) {
		this.numberEnd = numberEnd;
	}

	public byte[] getResponseSample() {
		return responseSample;
	}

	public void setResponseSample(byte[] responseSample) {
		this.responseSample = responseSample;
	}

	public String getExtParameters() {
		return extParameters;
	}

	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

}
