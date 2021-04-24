package com.aitrades.blockchain.eth.gateway.domain;

public class ApproveTransaction {

	private String id;
	private String orderId;
	private String approvedHash;
	private String status;
	private String contractAddressInteracted;
	private String publicKey;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getApprovedHash() {
		return approvedHash;
	}
	public void setApprovedHash(String approvedHash) {
		this.approvedHash = approvedHash;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContractAddressInteracted() {
		return contractAddressInteracted;
	}
	public void setContractAddressInteracted(String contractAddressInteracted) {
		this.contractAddressInteracted = contractAddressInteracted;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
}
