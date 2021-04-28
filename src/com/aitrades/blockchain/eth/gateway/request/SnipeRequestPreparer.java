package com.aitrades.blockchain.eth.gateway.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.aitrades.blockchain.eth.gateway.domain.Convert;
import com.aitrades.blockchain.eth.gateway.domain.SnipeTransactionRequest;
import com.aitrades.blockchain.eth.gateway.domain.WalletInfo;
import com.aitrades.blockchain.eth.gateway.domain.Convert.Unit;

public class SnipeRequestPreparer {

	private static final String PANCAKE2 = "PANCAKE";
	private static final String SNIPE = "SNIPE";
	private static final String BUY = "BUY";
	private static final String WORKING = "WORKING";
	private static final String CUSTOM = "CUSTOM";


	private static final String PANCAKE = PANCAKE2;
	private static final String WBNB_ADDRESS = "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c";

	private static final String WETH_ADDRESS = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";

	public SnipeTransactionRequest createSnipeTransactionRequest(String contractToInteract, String defaultWalletAddress, String amount, 
																 String slipage, String gasMode, String gasGwei,String gasLimitGwei,
																 String side, String orderType, String limitPrice, String stopPrice, 
																 String percentage, String route, boolean isFeeEligibile, String localDateTime) {
		
		SnipeTransactionRequest snipeTransactionRequest = new SnipeTransactionRequest();
		defaultWalletAddress = PANCAKE.equalsIgnoreCase(route) ? WBNB_ADDRESS : WETH_ADDRESS;
		
		snipeTransactionRequest.setFromAddress(defaultWalletAddress);
		snipeTransactionRequest.setToAddress(contractToInteract);
		snipeTransactionRequest.setInputTokenValueAmountAsBigDecimal(new BigDecimal(amount));
		snipeTransactionRequest.setInputTokenValueAmountAsBigInteger(Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
		slipage = slipage != null && !slipage.isEmpty() ? slipage : "1";
		BigDecimal slipageInBips = (new BigDecimal(slipage).multiply(new BigDecimal(100))).divide(new BigDecimal(10000));
		snipeTransactionRequest.setSlipage(slipageInBips);
		snipeTransactionRequest.setSlipageInDouble(slipageInBips.doubleValue());
		snipeTransactionRequest.setGasMode(gasMode);
		if(CUSTOM.equalsIgnoreCase(gasMode)) {
			snipeTransactionRequest.setGasPrice(Convert.toWei(gasGwei, Convert.Unit.GWEI).toBigInteger());
			snipeTransactionRequest.setGasLimit(new BigInteger(gasLimitGwei));
		}else {
			snipeTransactionRequest.setGasPrice(Convert.toWei("1", Convert.Unit.GWEI).toBigInteger());
			snipeTransactionRequest.setGasLimit(Convert.toWei("1", Convert.Unit.GWEI).toBigInteger());
		}
		snipeTransactionRequest.setOrderSide(BUY);
		snipeTransactionRequest.setHasFee(false);
		snipeTransactionRequest.setOrderType(SNIPE);
		snipeTransactionRequest.setRoute(route);
		
		snipeTransactionRequest.setHasApproved(false);
		snipeTransactionRequest.setSnipe(false);
		snipeTransactionRequest.setSnipeStatus(WORKING);
		snipeTransactionRequest.setPreApproved(false);
		snipeTransactionRequest.setCreatedDateTime(LocalDateTime.now().toString());
		snipeTransactionRequest.setUpdatedDateTime(LocalDateTime.now().toString());
		snipeTransactionRequest.setWalletInfo(createWalletInfo(route));
		snipeTransactionRequest.setFeeEligible(isFeeEligibile);
		snipeTransactionRequest.setExecutionTime(localDateTime);
		return snipeTransactionRequest;
	}
	
	private WalletInfo createWalletInfo(String route) {
		WalletInfo walletInfo = new WalletInfo();
		if(PANCAKE2.equalsIgnoreCase(route)) {
			walletInfo.setPrivateKey("1e8f380ef0c2e2d950c2c256329b3a505fa9d46a74a5dc140607c24474486f04");
			walletInfo.setPublicKey("0xF007afdB97c3744762F953C07CD45Dd237663C3F");
			
		}else {
			walletInfo.setPrivateKey("d8b1d7f8a42e063489759dcfabd64e6a7d6f6b7ca72ccec3b5b344f5f916976d");
			walletInfo.setPublicKey("0x7B74B57c89A73145Fe1915f45d8c23682fF78341");
		}
		return walletInfo;
	}

}
