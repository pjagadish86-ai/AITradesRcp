package com.aitrades.blockchain.eth.gateway.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonIgnoreProperties(ignoreUnknown = true)	
@JsonInclude(Include.NON_NULL)
public class EventState {
	
	private Boolean pairCreated;
	private Boolean reserves;
	private Boolean liquidity;
	private List<AdditionalProperty> addionalProperties;
	
	public boolean isPairCreated() {
		return pairCreated;
	}

	public void setPairCreated(boolean pairCreated) {
		this.pairCreated = pairCreated;
	}

	public boolean isReserves() {
		return reserves;
	}

	public void setReserves(boolean reserves) {
		this.reserves = reserves;
	}

	public boolean isLiquidity() {
		return liquidity;
	}

	public void setLiquidity(boolean liquidity) {
		this.liquidity = liquidity;
	}

	public List<AdditionalProperty> getAddionalProperties() {
		return addionalProperties;
	}

	public void setAddionalProperties(List<AdditionalProperty> addionalProperties) {
		this.addionalProperties = addionalProperties;
	}


}
