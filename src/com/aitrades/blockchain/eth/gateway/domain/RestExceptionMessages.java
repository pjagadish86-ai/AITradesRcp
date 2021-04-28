package com.aitrades.blockchain.eth.gateway.domain;

import java.io.Serializable;
import java.util.List;

public class RestExceptionMessages implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<RestExceptionMessage> exceptionMessages;

	public List<RestExceptionMessage> getExceptionMessages() {
		return exceptionMessages;
	}

	public void setExceptionMessages(List<RestExceptionMessage> exceptionMessages) {
		this.exceptionMessages = exceptionMessages;
	}
	
}
