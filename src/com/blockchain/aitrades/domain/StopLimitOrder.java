package com.blockchain.aitrades.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class StopLimitOrder {

	private String StopPrice;
	private BigInteger StopPriceBigInteger;
	private BigDecimal StopPriceBigDecimal;
	
	private String limitPrice;
	private BigInteger limitPriceBigInteger;
	private BigDecimal limitPriceBigDecimal;
	
	private List<AdditionalProperty> addionalProperties;

	public String getStopPrice() {
		return StopPrice;
	}

	public void setStopPrice(String stopPrice) {
		StopPrice = stopPrice;
	}

	public BigInteger getStopPriceBigInteger() {
		return StopPriceBigInteger;
	}

	public void setStopPriceBigInteger(BigInteger stopPriceBigInteger) {
		StopPriceBigInteger = stopPriceBigInteger;
	}

	public BigDecimal getStopPriceBigDecimal() {
		return StopPriceBigDecimal;
	}

	public void setStopPriceBigDecimal(BigDecimal stopPriceBigDecimal) {
		StopPriceBigDecimal = stopPriceBigDecimal;
	}

	public String getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}

	public BigInteger getLimitPriceBigInteger() {
		return limitPriceBigInteger;
	}

	public void setLimitPriceBigInteger(BigInteger limitPriceBigInteger) {
		this.limitPriceBigInteger = limitPriceBigInteger;
	}

	public BigDecimal getLimitPriceBigDecimal() {
		return limitPriceBigDecimal;
	}

	public void setLimitPriceBigDecimal(BigDecimal limitPriceBigDecimal) {
		this.limitPriceBigDecimal = limitPriceBigDecimal;
	}

	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}

	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}

}
