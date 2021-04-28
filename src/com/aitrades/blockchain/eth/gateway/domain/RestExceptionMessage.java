package com.aitrades.blockchain.eth.gateway.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonIgnoreProperties(ignoreUnknown = true)	
@JsonInclude(Include.NON_NULL)
public class RestExceptionMessage extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String errorCode;
	private String errorMessage;
	private String detailedErrorMessage;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getDetailedErrorMessage() {
		return detailedErrorMessage;
	}
	public void setDetailedErrorMessage(String detailedErrorMessage) {
		this.detailedErrorMessage = detailedErrorMessage;
	}
	
}
