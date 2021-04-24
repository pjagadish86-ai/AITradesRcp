package com.aitrades.blockchain.eth.gateway.domain;

import java.util.List;

public class Ticker {

	private String symbol;
	private String address;
	private String decimals;
	private List<AdditionalProperty> addionalProperties;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDecimals() {
		return decimals;
	}
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	
	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}
	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}
}
