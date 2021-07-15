package com.aitrades.blockchain.eth.gateway.clients;

import java.math.BigDecimal;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class PriceFeedOracleClient {
	
	private static final String ORDER_HISTORY = "http://localhost:8080/aitrades/eth-gateway/price/api/v1/price/{route}/{ticker}";
	
	private static HttpComponentsClientHttpRequestFactory httpRequestFactory = null;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(20000);
		httpRequestFactory.setConnectTimeout(20000);
		httpRequestFactory.setReadTimeout(20000);
	}
	
	public BigDecimal priceOracle(String route, String toAddressTicker) {
		RestTemplate restTemplate  = new RestTemplate(httpRequestFactory);
		try {
			BigDecimal price =  restTemplate.getForObject(ORDER_HISTORY, BigDecimal.class, route, toAddressTicker);
			if(price != null && price != null) {
				return price;
			}
		} catch (RestClientException e) {
			//e.printStackTrace();
		}finally {
			restTemplate = null;
		}
		return null;
	}
	
	private MultiValueMap<String, String> createSecurityHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("public-key", "publickey");
		httpHeaders.set("token", "securityToken");
		httpHeaders.set("auth-source-sys", "LDAP");
		return httpHeaders;
	}
}
