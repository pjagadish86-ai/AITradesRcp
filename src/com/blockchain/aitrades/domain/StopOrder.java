package com.blockchain.aitrades.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class StopOrder {
	private String StopPrice;
	private BigInteger StopPriceBigInteger;
	private BigDecimal StopPriceBigDecimal;
	
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

	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}

	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}
	
}
