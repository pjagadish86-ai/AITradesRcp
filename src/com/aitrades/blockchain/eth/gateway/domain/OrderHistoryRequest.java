package com.aitrades.blockchain.eth.gateway.domain;

public class OrderHistoryRequest {
	
	private String id;
	private String ethWalletPublicKey;
	private String bscWalletPublicKey;
	
	public String getEthWalletPublicKey() {
		return ethWalletPublicKey;
	}
	public void setEthWalletPublicKey(String ethWalletPublicKey) {
		this.ethWalletPublicKey = ethWalletPublicKey;
	}
	public String getBscWalletPublicKey() {
		return bscWalletPublicKey;
	}
	public void setBscWalletPublicKey(String bscWalletPublicKey) {
		this.bscWalletPublicKey = bscWalletPublicKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
