package com.aitrades.blockchain.eth.gateway.clients;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.domain.Convert;
import com.aitrades.blockchain.eth.gateway.domain.RetriggerSnipeOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RetriggerSnipeOrderClient {
	
	private static final String ORDER_HISTORY = "http://localhost:8080/aitrades/eth-gateway/snipe/api/v1/retriggerOrder";
	private static HttpComponentsClientHttpRequestFactory httpRequestFactory = null;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(20000);
		httpRequestFactory.setConnectTimeout(20000);
		httpRequestFactory.setReadTimeout(20000);
	}
	
	public String retriggerSnipeOrder(String id, String slipage, String gasPrice, String gasLimit) {
		RestTemplate restTemplate  = new RestTemplate(httpRequestFactory);
		try {
			HttpEntity<RetriggerSnipeOrder> httpEntity = new HttpEntity<RetriggerSnipeOrder>(prepareRetriggerSnipeOrder(id, slipage, gasPrice, gasLimit), createSecurityHeaders());
			ResponseEntity<RetriggerSnipeOrder> responseEntity =  restTemplate.exchange(ORDER_HISTORY, HttpMethod.POST, httpEntity, RetriggerSnipeOrder.class);
			if(responseEntity != null && responseEntity.getBody() != null) {
				return responseEntity.getBody().getSnipeOrderId();
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}finally {
			restTemplate = null;
		}
		return null;
	}

	private RetriggerSnipeOrder prepareRetriggerSnipeOrder(String id, String slipage, String gasPrice, String gasLimit) {
		RetriggerSnipeOrder retriggerSnipeOrder = new RetriggerSnipeOrder();

		BigDecimal slipageInBips = null;
		if(slipage != null && !slipage.isEmpty()) {
			slipageInBips = (new BigDecimal(slipage).multiply(new BigDecimal(100))).divide(new BigDecimal(10000));
			retriggerSnipeOrder.setSlipage(slipageInBips);
			retriggerSnipeOrder.setSlipageInDouble(slipageInBips.doubleValue());
		}
		retriggerSnipeOrder.setParentSnipeOrderId(id);
		if(gasPrice != null && !gasPrice.isEmpty()) {
			retriggerSnipeOrder.setGasPrice(Convert.toWei(gasPrice, Convert.Unit.GWEI).toBigInteger());
		}
		if(gasLimit != null && !gasLimit.isEmpty()) {
			retriggerSnipeOrder.setGasLimit(new BigInteger(gasLimit));
		}
		return retriggerSnipeOrder;
	}

	private MultiValueMap<String, String> createSecurityHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("public-key", "publickey");
		httpHeaders.set("token", "securityToken");
		httpHeaders.set("auth-source-sys", "LDAP");
		return httpHeaders;
	}
	
}
