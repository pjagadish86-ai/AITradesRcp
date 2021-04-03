package com.blockchain.aitrades.parts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.blockchain.aitrades.domain.SnipeTransactionRequest;
import com.blockchain.aitrades.domain.WalletInfo;

public class SnipeRequestPreparer {

	private static final String SNIPE = "SNIPE";
	private static final String BUY = "BUY";
	private static final String WORKING = "WORKING";
	private static final String CUSTOM = "CUSTOM";
	
	public SnipeTransactionRequest createSnipeTransactionRequest(String fromAddress, String toAddress, String amount, 
							 String slipage, String gasMode, String gasGwei,String gasLimitGwei,
							 String side, String orderType, String limitPrice, String stopPrice, 
							 String percentage, String route) {
		
		SnipeTransactionRequest snipeTransactionRequest = new SnipeTransactionRequest();

		snipeTransactionRequest.setFromAddress(fromAddress);
		snipeTransactionRequest.setToAddress(toAddress);
		snipeTransactionRequest.setInputTokenValueAmountAsBigDecimal(new BigDecimal(amount));
		snipeTransactionRequest.setInputTokenValueAmountAsBigInteger(Convert.toWei(amount, Convert.Unit.GWEI).toBigInteger());
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
		snipeTransactionRequest.setWalletInfo(createWalletInfo());
		return snipeTransactionRequest;
	}
	
	private WalletInfo createWalletInfo() {
		WalletInfo walletInfo = new WalletInfo();
		walletInfo.setPrivateKey("b05ae23814ff6cba5e640d4ac3dad7ead15e3ea73089077c01e784a97ecf4239");
		walletInfo.setPublicKey("0xa827A97B88a462ECE07f53bc9A104c0e71983004");
		return walletInfo;
	}

}
