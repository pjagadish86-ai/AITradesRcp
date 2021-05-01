package com.aitrades.blockchain.eth.gateway.clients;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.domain.BlockchainExchange;
import com.aitrades.blockchain.eth.gateway.domain.BlockchainExchanges;

public class BlockchainExchangesClient {

	private static final String BLOCKCHAIN_EXCHANGES_CLIENT = "http://localhost:8080/aitrades/eth-gateway/exchanges/api/v1/exchanges";
	private static HttpComponentsClientHttpRequestFactory httpRequestFactory = null;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(20000);
		httpRequestFactory.setConnectTimeout(20000);
		httpRequestFactory.setReadTimeout(20000);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<BlockchainExchange> fetchBlockchainExchanges() {
		RestTemplate restTemplate  = new RestTemplate(httpRequestFactory);
		try {
			 HttpEntity httpEntity = new HttpEntity(createSecurityHeaders());
			ResponseEntity<BlockchainExchanges> responseEntity =  restTemplate.exchange(BLOCKCHAIN_EXCHANGES_CLIENT, HttpMethod.GET, httpEntity, BlockchainExchanges.class);
			if(responseEntity != null && responseEntity.getBody() != null) {
				return responseEntity.getBody().getBlockchainExchanges();
			}
		} catch (RestClientException e) {
			e.printStackTrace();
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
