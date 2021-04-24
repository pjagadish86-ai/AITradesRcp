package com.aitrades.blockchain.eth.gateway.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class TickerEntity {
	
	private Ticker ticker;
	private String amount;
	private BigInteger amountAsBigInteger;
	private BigDecimal amountAsBigDecimal;
	private List<AdditionalProperty> addionalProperties;

	public Ticker getTicker() {
		return ticker;
	}

	public void setTicker(Ticker ticker) {
		this.ticker = ticker;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public BigInteger getAmountAsBigInteger() {
		return amountAsBigInteger;
	}

	public void setAmountAsBigInteger(BigInteger amountAsBigInteger) {
		this.amountAsBigInteger = amountAsBigInteger;
	}

	public BigDecimal getAmountAsBigDecimal() {
		return amountAsBigDecimal;
	}

	public void setAmountAsBigDecimal(BigDecimal amountAsBigDecimal) {
		this.amountAsBigDecimal = amountAsBigDecimal;
	}

	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}

	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}

}
