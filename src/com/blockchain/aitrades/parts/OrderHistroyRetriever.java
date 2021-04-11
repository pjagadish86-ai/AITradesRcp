package com.blockchain.aitrades.parts;

import java.util.ArrayList;
import java.util.List;

public class OrderHistroyRetriever {

	public List<OrderHistory> retrieveOrderHistroy(String ethWalletPublicKey, String bscWalletPublicKey) {
		List<OrderHistory> histories = new ArrayList<>();
		System.out.println("Refresh clicked");
		OrderHistory history = new OrderHistory();
		history.setId("ID");
		history.setRoute("PANCAKE");
		history.setTradetype("SNIPE");
		history.setFromTickerSymbol("BNB");
		history.setInput("0.1");
		history.setToTickerSymbol("ORK");
		history.setOutput("312.12");
		history.setExecutedprice("$2.19");
		history.setOrderstate("FILLED");
		history.setApprovedhash("0x050182d8b62d93f7d893a4ab3346aaa818c53eae~UNISWAP~0xad6d458402f60fd3bd25163575031acdce07538d");
		history.setApprovedhashStatus("SUCCESS");
		history.setSwappedhash("0x64e71aa7582b1b11a4f5547b01bfeb978b76c35e80d0f3e61c67575d3fa040cd");
		history.setSwappedhashStatus("SUCCESS");
		history.setOrderside("BUY");
		history.setErrormessage("");
		histories.add(history);
		return histories;
	}

}
