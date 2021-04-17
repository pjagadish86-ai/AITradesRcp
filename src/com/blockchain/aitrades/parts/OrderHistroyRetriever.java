package com.blockchain.aitrades.parts;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.framework.UUIDGenerator;

public class OrderHistroyRetriever {
	
	private static final String ORDER_HISTORY = "http://localhost:8080/aitrades/eth-gateway/order/api/v1/orderHistory";

	public List<OrderHistory> retrieveOrderHistroy(String ethWalletPublicKey, String bscWalletPublicKey) {
		return fetchOrderHistories(ethWalletPublicKey, bscWalletPublicKey);
	}
	
	private List<OrderHistory> fetchOrderHistories(String ethWalletPublicKey, String bscWalletPublicKey) {
		List<OrderHistory> respose = null;
		RestTemplate restTemplate = null;
		try {
			HttpEntity<OrderHistoryRequest> httpEntity = new HttpEntity<OrderHistoryRequest>(prepareOrderHistoryRequest(ethWalletPublicKey, bscWalletPublicKey),createSecurityHeaders());
			restTemplate = new RestTemplate();
			ResponseEntity<OrderHistories> responseEntity =  restTemplate.exchange(ORDER_HISTORY, HttpMethod.POST, httpEntity, OrderHistories.class);
			return responseEntity.getBody().getOrderHistories();
		} catch (RestClientException e) {
			e.printStackTrace();
		}finally {
			restTemplate = null;
		}
		return Collections.emptyList();
	}

	private OrderHistoryRequest prepareOrderHistoryRequest(String ethWalletPublicKey, String bscWalletPublicKey) {
		OrderHistoryRequest orderHistoryRequest = new OrderHistoryRequest();
		orderHistoryRequest.setId(UUIDGenerator.nextHex(UUIDGenerator.TYPE1));
		orderHistoryRequest.setEthWalletPublicKey(ethWalletPublicKey);
		orderHistoryRequest.setBscWalletPublicKey(bscWalletPublicKey);
		return orderHistoryRequest;
	}

	private MultiValueMap<String, String> createSecurityHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("public-key", "publickey");
		httpHeaders.set("token", "securityToken");
		httpHeaders.set("auth-source-sys", "LDAP");
		return httpHeaders;
	}
	
}
