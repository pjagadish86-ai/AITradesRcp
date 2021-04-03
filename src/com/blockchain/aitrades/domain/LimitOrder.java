package com.blockchain.aitrades.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class LimitOrder {

	private String limitPrice;
	private BigInteger limitPriceBigInteger;
	private BigDecimal limitPriceBigDecimal;
	
	private List<AdditionalProperty> addionalProperties;

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
