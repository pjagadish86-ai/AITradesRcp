package com.aitrades.blockchain.eth.gateway.clients;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.domain.ApproveRequest;

public class ApproveTransactionClient {

	private static final String ORDER_HISTORY = "http://localhost:8080/aitrades/eth-gateway/snipe/api/v1/approve";
	private static HttpComponentsClientHttpRequestFactory httpRequestFactory = null;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(20000);
		httpRequestFactory.setConnectTimeout(20000);
		httpRequestFactory.setReadTimeout(20000);
	}
	
	public String approveRequest(String id) {
		RestTemplate restTemplate  = new RestTemplate(httpRequestFactory);
		try {
			HttpEntity<ApproveRequest> httpEntity = new HttpEntity<ApproveRequest>(prepareApproveRequest(id), createSecurityHeaders());
			ResponseEntity<String> responseEntity =  restTemplate.exchange(ORDER_HISTORY, HttpMethod.POST, httpEntity, String.class);
			if(responseEntity != null && responseEntity.getBody() != null) {
				return responseEntity.getBody();
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}finally {
			restTemplate = null;
		}
		return null;
	}

	private ApproveRequest prepareApproveRequest(String id) {
		ApproveRequest approveRequest = new ApproveRequest();
		approveRequest.setId(id);
		return approveRequest;
	}

	private MultiValueMap<String, String> createSecurityHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("public-key", "publickey");
		httpHeaders.set("token", "securityToken");
		httpHeaders.set("auth-source-sys", "LDAP");
		return httpHeaders;
	}
}
