package com.aitrades.blockchain.eth.gateway.clients;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.domain.OrderHistories;
import com.aitrades.blockchain.eth.gateway.domain.OrderHistory;
import com.aitrades.blockchain.eth.gateway.domain.OrderHistoryRequest;
import com.aitrades.blockchain.framework.UUIDGenerator;

public class OrderHistroyRetrieverClient {
	
	private static final String ORDER_HISTORY = "http://localhost:8080/aitrades/eth-gateway/order/api/v1/orderHistory";
	private static HttpComponentsClientHttpRequestFactory httpRequestFactory = null;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(20000);
		httpRequestFactory.setConnectTimeout(20000);
		httpRequestFactory.setReadTimeout(20000);
	}
	public List<OrderHistory> retrieveOrderHistroy(String ethWalletPublicKey, String bscWalletPublicKey) {
		return fetchOrderHistories(ethWalletPublicKey, bscWalletPublicKey);
	}
	
	private List<OrderHistory> fetchOrderHistories(String ethWalletPublicKey, String bscWalletPublicKey) {
	     RestTemplate restTemplate  = new RestTemplate(httpRequestFactory);
		try {
			HttpEntity<OrderHistoryRequest> httpEntity = new HttpEntity<OrderHistoryRequest>(prepareOrderHistoryRequest(ethWalletPublicKey, bscWalletPublicKey),createSecurityHeaders());
			ResponseEntity<OrderHistories> responseEntity =  restTemplate.exchange(ORDER_HISTORY, HttpMethod.POST, httpEntity, OrderHistories.class);
			System.out.println("ping good");
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
