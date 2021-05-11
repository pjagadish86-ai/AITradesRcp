package com.aitrades.blockchain.eth.gateway.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Combo;

import com.aitrades.blockchain.eth.gateway.domain.Convert;
import com.aitrades.blockchain.eth.gateway.domain.Gas;
import com.aitrades.blockchain.eth.gateway.domain.LimitOrder;
import com.aitrades.blockchain.eth.gateway.domain.LimitTrailingStop;
import com.aitrades.blockchain.eth.gateway.domain.Order;
import com.aitrades.blockchain.eth.gateway.domain.OrderEntity;
import com.aitrades.blockchain.eth.gateway.domain.OrderSide;
import com.aitrades.blockchain.eth.gateway.domain.OrderType;
import com.aitrades.blockchain.eth.gateway.domain.Slipage;
import com.aitrades.blockchain.eth.gateway.domain.StopLimitOrder;
import com.aitrades.blockchain.eth.gateway.domain.StopOrder;
import com.aitrades.blockchain.eth.gateway.domain.Ticker;
import com.aitrades.blockchain.eth.gateway.domain.TickerEntity;
import com.aitrades.blockchain.eth.gateway.domain.TrailingStopOrder;
import com.aitrades.blockchain.eth.gateway.domain.WalletInfo;

public class OrderRequestPreparer {

	private static final String PANCAKE = "PANCAKE";
	private static final String WORKING = "WORKING";
	private static final String CUSTOM = "CUSTOM";
	
	private static final String WBNB_ADDRESS = "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c";
	private static final String WETH_ADDRESS = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";
	private static final String WFTM_ADDRESS ="0x21be370d5312f44cb42ce377bc9b8a0cef1a4c83";
	private static final String WMATIC_ADDRESS ="0x21be370d5312f44cb42ce377bc9b8a0cef1a4c83";
	
	static Map<String, String> wrappedMap = new HashMap<>();
	
	static {
		wrappedMap.put("1", WETH_ADDRESS);
		wrappedMap.put("2", WETH_ADDRESS);
		wrappedMap.put("3", WBNB_ADDRESS);
		wrappedMap.put("4", WFTM_ADDRESS);
		wrappedMap.put("5", WFTM_ADDRESS);
		wrappedMap.put("6", WFTM_ADDRESS);
		wrappedMap.put("7", WFTM_ADDRESS);
		wrappedMap.put("8", WMATIC_ADDRESS);
	}
	
	public Order createOrder(String contractToInteract, String defaultWalletAddress, String amount, 
							 String slipage, String gasMode, String gasGwei, String gasLimitGwei,
							 String side, String orderType, String limitPrice, String stopPrice, 
							 String percentage, String route, boolean isFeeEligibile, Combo blockChainComboitems) {
		
		Order order = new Order();
		
		if(OrderSide.BUY.name().equalsIgnoreCase(side)) {
			contractToInteract = wrappedMap.get(route);
		}
		if(OrderSide.SELL.name().equalsIgnoreCase(side)) {
			defaultWalletAddress = wrappedMap.get(route);
		}
		
		order.setFrom(createTickerEntity(route, amount, contractToInteract, side));
		order.setTo(createTickerEntity(route, null, defaultWalletAddress, side));
		order.setOrderEntity(createOrderEntity(orderType, side, limitPrice, stopPrice, percentage));
		order.setSlippage(createSlipage(slipage));
		
		order.setGasMode(gasMode);
		if(CUSTOM.equalsIgnoreCase(gasMode)) {
			order.setGasPrice(createGas(gasGwei));
			order.setGasLimit(createGasLimit(gasLimitGwei));
		}else {
			order.setGasPrice(createGas("1"));
			order.setGasLimit(createGas("1"));
		}
		order.setWalletInfo(createWalletInfo(blockChainComboitems));
		order.setRoute(route);
		order.setFee(isFeeEligibile);
		return order;
	}
	
	private Gas createGasLimit(String gasLimitGwei) {
		Gas gas = new Gas();
		gas.setValue(gasLimitGwei);
		gas.setValueBigInteger(new BigInteger(gasLimitGwei));
		return gas;
	}

	private WalletInfo createWalletInfo(Combo blockChainComboitems) {
		WalletInfo walletInfo = new WalletInfo();

		if("ETH".equalsIgnoreCase(blockChainComboitems.getText())){
			walletInfo.setPrivateKey("d8b1d7f8a42e063489759dcfabd64e6a7d6f6b7ca72ccec3b5b344f5f916976d");
			walletInfo.setPublicKey("0x7B74B57c89A73145Fe1915f45d8c23682fF78341");
		}else if("BSC".equalsIgnoreCase(blockChainComboitems.getText())){
			walletInfo.setPrivateKey("1e8f380ef0c2e2d950c2c256329b3a505fa9d46a74a5dc140607c24474486f04");
			walletInfo.setPublicKey("0xF007afdB97c3744762F953C07CD45Dd237663C3F");//  this is should be defaulted to BSC, FTM, PLOYGON and SHOULD be included to have ETH 
		}else if("FTM".equalsIgnoreCase(blockChainComboitems.getText())){
			walletInfo.setPrivateKey("179d5ad28dc5c446d4bf7d8ea9f3f0ebcbe31d00941ebdd8440662e6a0061b94");
			walletInfo.setPublicKey("0x7aafFCCF53f113016d6B5aaF89C7c0C8aFd6c22A");//  this is should be defaulted to BSC, FTM, PLOYGON and SHOULD be included to have ETH 
		}
		return walletInfo;
	}

	private Gas createGas(String gasGwei) {
		Gas gas = new Gas();
		gas.setValue(gasGwei);
		gas.setValueBigInteger(Convert.toWei(gasGwei, Convert.Unit.GWEI).toBigInteger());
		return gas;
		
	}

	private OrderEntity createOrderEntity(String orderType, String side, String limitPrice, String stopPrice, String percentage) {
		OrderEntity orderEntity = new OrderEntity();
		
		orderEntity.setOrderSide(side);
		orderEntity.setOrderState(WORKING);
		orderEntity.setOrderType(orderType);
		
		if(OrderType.LIMIT.name().equalsIgnoreCase(orderType)) {
			orderEntity.setLimitOrder(createLimitOrder(limitPrice));
		}
		
		else if(OrderType.STOPLOSS.name().equalsIgnoreCase(orderType)) {
			orderEntity.setStopOrder(createStopLossOrder(stopPrice));
		}
		
		else if(OrderType.STOPLIMIT.name().equalsIgnoreCase(orderType)) {
			orderEntity.setStopLimitOrder(createStopLimitOrder(limitPrice, stopPrice));
		}
		
		else if(OrderType.TRAILLING_STOP.name().equalsIgnoreCase(orderType)) {
			orderEntity.setTrailingStopOrder(createTrailingStopOrder(percentage));
		}
		
		else if(OrderType.LIMIT_TRAILLING_STOP.name().equalsIgnoreCase(orderType)) {
			orderEntity.setLimitTrailingStop(createLimitTrailingStop(limitPrice, percentage));
		}
		
		return orderEntity;
	}

	private LimitTrailingStop createLimitTrailingStop(String limitPrice, String percentage) {
		LimitTrailingStop limitTrailingStop = new LimitTrailingStop();
		limitTrailingStop.setLimitPrice(limitPrice);
		BigDecimal limitPriceAsBigDecimal = new BigDecimal(limitPrice).setScale(2, RoundingMode.DOWN);
		limitTrailingStop.setLimitPriceBigDecimal(limitPriceAsBigDecimal);
		limitTrailingStop.setLimitPriceBigInteger(limitPriceAsBigDecimal.toBigInteger());
		limitTrailingStop.setTrailingStopPercent(percentage);
		limitTrailingStop.setTrailingStopPercentBigDecimal(new BigDecimal(percentage).setScale(2, RoundingMode.DOWN));
		return limitTrailingStop;
	}

	private TrailingStopOrder createTrailingStopOrder(String percentage) {
		TrailingStopOrder trailingStopOrder = new TrailingStopOrder();
		trailingStopOrder.setTrailingStopPercent(percentage);
		trailingStopOrder.setTrailingStopPercentBigDecimal(new BigDecimal(percentage).setScale(2, RoundingMode.DOWN));
		return trailingStopOrder;
	}

	private StopLimitOrder createStopLimitOrder(String limitPrice, String stopPrice) {
		StopLimitOrder stopLimitOrder = new StopLimitOrder();
		
		BigDecimal stopOrderPriceAsBigDecimal = new BigDecimal(stopPrice).setScale(2, RoundingMode.DOWN);
		stopLimitOrder.setStopPrice(stopPrice);
		stopLimitOrder.setStopPriceBigDecimal(stopOrderPriceAsBigDecimal);
		stopLimitOrder.setStopPriceBigInteger(stopOrderPriceAsBigDecimal.toBigInteger());
		
		stopLimitOrder.setLimitPrice(limitPrice);
		BigDecimal limitPriceAsBigDecimal = new BigDecimal(limitPrice).setScale(2, RoundingMode.DOWN);
		stopLimitOrder.setLimitPriceBigDecimal(limitPriceAsBigDecimal);
		stopLimitOrder.setLimitPriceBigInteger(limitPriceAsBigDecimal.toBigInteger());
		return stopLimitOrder;
	}

	private StopOrder createStopLossOrder(String stopPrice) {
		BigDecimal stopOrderPriceAsBigDecimal = new BigDecimal(stopPrice).setScale(2, RoundingMode.DOWN);
		StopOrder stopOrder = new StopOrder();
		stopOrder.setStopPrice(stopPrice);
		stopOrder.setStopPriceBigDecimal(stopOrderPriceAsBigDecimal);
		stopOrder.setStopPriceBigInteger(stopOrderPriceAsBigDecimal.toBigInteger());
		return stopOrder;
	}

	private LimitOrder createLimitOrder(String limitPrice) {
		LimitOrder limitOrder = new LimitOrder();
		limitOrder.setLimitPrice(limitPrice);
		BigDecimal limitPriceAsBigDecimal = new BigDecimal(limitPrice).setScale(2, RoundingMode.DOWN);
		limitOrder.setLimitPriceBigDecimal(limitPriceAsBigDecimal);
		limitOrder.setLimitPriceBigInteger(limitPriceAsBigDecimal.toBigInteger());
		return limitOrder;
	}

	private Slipage createSlipage(String slipagePercent) {
		if(slipagePercent == null || slipagePercent.isEmpty()) {
			slipagePercent ="1.0";
		}
		
		Slipage slipage  = new Slipage();
		slipage.setSlipagePercent(slipagePercent);
		BigDecimal slipageAsBigDecimal = new BigDecimal(slipagePercent).setScale(2, RoundingMode.DOWN);
		if(slipageAsBigDecimal.compareTo(new BigDecimal("0.5")) <= 0) {
			slipageAsBigDecimal = BigDecimal.ONE;
		}
		slipage.setSlipapagePercentAsBigDecimal(slipageAsBigDecimal);
		slipage.setSlipageInBips((slipageAsBigDecimal.multiply(new BigDecimal(100))).divide(new BigDecimal(10000)));
		slipage.setSlipageInBipsInDouble((slipageAsBigDecimal.multiply(new BigDecimal(100))).divide(new BigDecimal(10000)).doubleValue());
		return slipage;
	}

	private static TickerEntity createTickerEntity(String route, String amount, String address, String side) {
		TickerEntity tickerEntity = new TickerEntity();
		if(amount != null) {
			tickerEntity.setAmount(amount);
			BigDecimal amountAsBigdecimal = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
			tickerEntity.setAmountAsBigDecimal(amountAsBigdecimal);
			tickerEntity.setAmountAsBigInteger(Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
		}
		tickerEntity.setTicker(createTicker(address, side));
		return tickerEntity;
	}

	private static Ticker createTicker(String address, String side) {
		Ticker ticker = new Ticker();
		ticker.setAddress(address);
		return ticker;
	}
	

}
