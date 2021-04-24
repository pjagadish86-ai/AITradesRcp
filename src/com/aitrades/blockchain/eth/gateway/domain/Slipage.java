package com.aitrades.blockchain.eth.gateway.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class Slipage {

	private String slipagePercent;
	private BigInteger slipagePercentAsBigInteger;
	private BigDecimal slipageInBips;
	private Double slipageInBipsInDouble;
	private BigDecimal slipapagePercentAsBigDecimal;
	
	private List<AdditionalProperty> addionalProperties;

	public String getSlipagePercent() {
		return slipagePercent;
	}

	public void setSlipagePercent(String slipagePercent) {
		this.slipagePercent = slipagePercent;
	}

	public BigInteger getSlipagePercentAsBigInteger() {
		return slipagePercentAsBigInteger;
	}

	public void setSlipagePercentAsBigInteger(BigInteger slipagePercentAsBigInteger) {
		this.slipagePercentAsBigInteger = slipagePercentAsBigInteger;
	}

	public BigDecimal getSlipapagePercentAsBigDecimal() {
		return slipapagePercentAsBigDecimal;
	}

	public void setSlipapagePercentAsBigDecimal(BigDecimal slipapagePercentAsBigDecimal) {
		this.slipapagePercentAsBigDecimal = slipapagePercentAsBigDecimal;
	}

	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}

	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}

	public BigDecimal getSlipageInBips() {
		return slipageInBips;
	}

	public void setSlipageInBips(BigDecimal slipageInBips) {
		this.slipageInBips = slipageInBips;
	}

	public Double getSlipageInBipsInDouble() {
		return slipageInBipsInDouble;
	}

	public void setSlipageInBipsInDouble(Double slipageInBipsInDouble) {
		this.slipageInBipsInDouble = slipageInBipsInDouble;
	}
	
	

}
