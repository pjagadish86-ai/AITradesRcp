package com.aitrades.blockchain.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestCollection<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6443141905409828191L;
	
	private List<T> content = new ArrayList<T>();
	private final String message;

	@JsonCreator
	public RestCollection(@JsonProperty("content") List<T> content, @JsonProperty("message") String message) {
		this.content = content;
		this.message = message;
	}

	public List<T> getContent() {
		return content;
	}

	public String getMessage() {
		return message;
	}
	
}
