package com.blockchain.aitrades.parts;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.domain.Order;
import com.aitrades.blockchain.eth.gateway.domain.OrderType;
import com.aitrades.blockchain.eth.gateway.domain.SnipeTransactionRequest;
import com.aitrades.blockchain.eth.gateway.request.OrderRequestPreparer;
import com.aitrades.blockchain.eth.gateway.request.SnipeRequestPreparer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AITradesV2 {
	
	private static final String CUSTOM = "CUSTOM";
	private static final String CREATE_ORDER = "http://localhost:8080/aitrades/eth-gateway/order/api/v1/createOrder";
	private static final String SNIPE_ORDER = "http://localhost:8080/aitrades/eth-gateway/snipe/api/v1/snipeOrder";
	
	private static final String ETH_ADDRESS = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";

	private static final String BNB_ADDRESS = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";

	private Button tradeButton = null;
	private Button snipeButton = null;
	private String tradeOrSnipe=null;
	Text gasGweiText = null;
	Text gasLimitText = null;
	private Button buyButton = null;
	private Button sellButton = null;
	private String buyOrSell=null;
	
	private String orderTypeSelected = null;
	private String dexRoute = "UNISWAP";
	Combo gasModeComboitems = null;
	//snipe order : 
	Text fromAddress = null;
	Text toAddressText = null;
	Text limitPriceText = null;
	Text stopPriceText = null;
	Text percentText = null;
	Text takeProfitOrderLimit = null;
	Button placeOrderButton = null;
	Combo orderTypeLabelComboitems = null;
	
	boolean isFeeEligibile = false;
	
	boolean isExecition = false;
	DateTime time = null;
	String executionTime = "";
	Button isExecutionOrderCheckBox = null;
	
	Device device = Display.getCurrent();
	RGB rgbRed = new RGB(255, 0, 0);
	Color redColor = new Color(device, rgbRed);
	
	RGB rgbblack = new RGB(90,90,90);
	Color black = new Color(device, rgbblack);
	
	RGB rgbgreyColor = new RGB(124, 128, 126);
	Color greyColor = new Color(device, rgbgreyColor);
	
	RGB rgbGreen = new RGB(0, 219, 134);
	Color greenColor = new Color(device, rgbGreen);
	
	RGB rgbLightGreen = new RGB(145,193,83);
	Color lightGreenColor = new Color(device, rgbLightGreen);
	
	
	RGB rgbBlue = new RGB(0, 219, 134);
	Color blueColor = new Color(device, rgbBlue);
	
	RGB rgbLightBlue = new RGB(31,105,119);
	Color lightBlueColor = new Color(device, rgbLightBlue);
	
	
	
	RGB rgbWhite = new RGB(255, 255, 255);
	Color whiteColor = new Color(device, rgbWhite);
	
	String localDateTime =  null;
	String localDateTime1 =  null;
	
	@PostConstruct
	public void createComposite(Composite parent1) {
		
		parent1.setLayout(new GridLayout(1, true));
		parent1.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		GridData data = new GridData(SWT.NONE, SWT.NONE, true, true);
		parent1.setLayoutData(data);
		data.minimumHeight=100;
		
		Composite parent = new Composite(parent1, SWT.NONE);
		parent.setLayout(new GridLayout(1, true));
		parent.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		parent.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));

		Composite topBtnComposite = new Composite(parent, SWT.NONE);
		topBtnComposite.setLayout( new GridLayout(2, true));
		topBtnComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));
		
		tradeButton = new Button(topBtnComposite, SWT.PUSH);
		snipeButton = new Button(topBtnComposite, SWT.PUSH);
	
		tradeButton.setText("                                              TRADE                                        ");
		tradeButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		tradeButton.setBackground(black);
		tradeButton.addSelectionListener(getButtonSelectionAdapter());
		
		snipeButton.setText("                                              SNIPE                                         ");
		snipeButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		snipeButton.setBackground(black);
		snipeButton.addSelectionListener(getButtonSelectionAdapter());
		
		
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(2, true));
		topComposite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));
		
		
		Label routeLabel = new Label(topComposite, SWT.NONE);
		routeLabel.setText("DEX                 ");
		routeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		Combo routeComboitems = new Combo(topComposite, SWT.DROP_DOWN);
		String[] routetems = new String[] { "UNISWAP", "SUSHI", "PANCAKE"};
		routeComboitems.setItems(routetems);
		routeComboitems.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		routeComboitems.select(0);
		routeComboitems.pack();
		
		Label fromLabel = new Label(topComposite, SWT.NONE);
		fromLabel.setText("FROM ADDR             ");
		fromLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		fromAddress = new Text(topComposite, SWT.NONE);
		fromAddress.setLayoutData(new GridData(300, 20));
		
		Label inputAmountLabel = new Label(topComposite, SWT.NONE);
		inputAmountLabel.setText("INPUT AMOUNT   ");
		inputAmountLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		Text inputAmountText = new Text(topComposite, SWT.NONE);
		inputAmountText.setLayoutData(new GridData(100, 20));
		
		Label toAddressLabel = new Label(topComposite, SWT.NONE);
		toAddressLabel.setText("TO ADDR          ");
	
		toAddressText = new Text(topComposite, SWT.NONE);
		toAddressText.setLayoutData(new GridData(300, 20));
		
		toAddressLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		fromAddress.setMessage("ETH/WETH/BNB defaulted based on DEX ROUTE-BUY");
		toAddressText.setMessage("ETH/WETH/BNB defaulted based on DEX ROUTE-SELL");
		
		//Slippage combo
		Label slipagelabel = new Label(topComposite, SWT.NONE);
		slipagelabel.setText("SLIPAGE            ");
		Text slipagelabelText = new Text(topComposite, SWT.NONE);
		slipagelabelText.setLayoutData(new GridData(100, 20));
		slipagelabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label FeeLabel = new Label(topComposite, SWT.NONE);
		FeeLabel.setText("FEE                     ");
		FeeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		Button hasFee = new Button(topComposite, SWT.CHECK);
		hasFee.setText("Fee");
		hasFee.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent event) {
		    	Button btn = (Button) event.getSource();
		    	isFeeEligibile = btn.getSelection();
		    }
		});
		
		Label gasModeLabel = new Label(topComposite, SWT.NONE);
		gasModeLabel.setText("GAS MODE           ");
		gasModeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		gasModeComboitems = new Combo(topComposite, SWT.DROP_DOWN);
		String[] gasModetems = new String[] { "ULTRA", "FASTEST", "FAST", "STANDARD", "SAFELOW", CUSTOM};
		gasModeComboitems.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		gasModeComboitems.setItems(gasModetems);
		gasModeComboitems.select(1);
		gasModeComboitems.pack();
		
		
		Label gasMinLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		gasMinLabel.setText("GAS PRICE           ");
		gasGweiText = new Text(topComposite, SWT.NONE);
		gasGweiText.setLayoutData(new GridData(100, 20));
		gasMinLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label gasLimitLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		gasLimitLabel.setText("GAS LIMIT           ");
		gasLimitText = new Text(topComposite, SWT.NONE);
		gasLimitText.setLayoutData(new GridData(100, 20));
		gasLimitLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		gasGweiText.setEnabled(false);
		gasLimitText.setEditable(false);
		gasGweiText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
		gasLimitText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
		
		gasModeComboitems.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (((Combo)e.getSource()).getText().equalsIgnoreCase(CUSTOM)){
					gasGweiText.setEnabled(true);
					gasLimitText.setEditable(true);
					gasGweiText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					gasLimitText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
				}else {
					gasGweiText.setEnabled(false);
					gasLimitText.setEditable(false);
					gasGweiText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					gasLimitText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
				}
			}

		});
		
		
		routeComboitems.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dexRoute = ((Combo)e.getSource()).getText().toString();
				if("PANCAKE".equalsIgnoreCase(dexRoute)) {
					gasModeComboitems.select(5);
					gasModeComboitems.setEnabled(false);
					gasGweiText.setEditable(true);
					gasLimitText.setEditable(true);
					gasGweiText.setEnabled(true);
					gasLimitText.setEnabled(true);
					gasGweiText.setEnabled(true);
					gasLimitText.setEditable(true);
					gasGweiText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					gasLimitText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					gasGweiText.setText("10");
					gasLimitText.setText("217216");
				}else {
					gasModeComboitems.setEnabled(true);
				}
			}
		});
		
		Label orderTypeLabel = new Label(topComposite, SWT.NONE);
		orderTypeLabel.setText("ORDER TYPE       ");
		orderTypeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		orderTypeLabelComboitems = new Combo(topComposite, SWT.DROP_DOWN);
		String[] orderTypeLabelItems = new String[] { "MARKET", "LIMIT", "STOPLOSS", "STOPLIMIT", "TRAILLING_STOP", "LIMIT_TRAILLING_STOP"};
		orderTypeLabelComboitems.setItems(orderTypeLabelItems);
		orderTypeLabelComboitems.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		orderTypeLabelComboitems.pack();

		Label priceLabel = new Label(topComposite, SWT.NONE);// if LIMIT or stop or stop limit gas mode is custom then enable gasprice
		priceLabel.setText("LIMIT PRICE          ");
		limitPriceText = new Text(topComposite, SWT.NONE);
		limitPriceText.setLayoutData(new GridData(100, 20));
		priceLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label stopPriceLabel = new Label(topComposite, SWT.NONE);// if LIMIT or stop or stop limit gas mode is custom then enable gasprice
		stopPriceLabel.setText("STOP PRICE       ");
		stopPriceText = new Text(topComposite, SWT.NONE);
		stopPriceText.setLayoutData(new GridData(100, 20));
		stopPriceLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label percentLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		percentLabel.setText("TRAILING PERCENT   ");
		percentText = new Text(topComposite, SWT.NONE);
		percentText.setLayoutData(new GridData(100, 20));
		percentLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label takeProfitOrderLimitLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		takeProfitOrderLimitLabel.setText("Take Profit Limt");
		takeProfitOrderLimit = new Text(topComposite, SWT.NONE);
		takeProfitOrderLimit.setLayoutData(new GridData(100, 20));
		takeProfitOrderLimitLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		orderTypeLabelComboitems.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String orderType = ((Combo)e.getSource()).getText();
				orderTypeSelected = orderType;
				if (orderType.equalsIgnoreCase(OrderType.LIMIT.name())){
					
					limitPriceText.setEnabled(true);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					
					limitPriceText.setEditable(true);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					
				}
				
				else if(orderType.equalsIgnoreCase(OrderType.STOPLOSS.name())) {
					limitPriceText.setEnabled(false);
					stopPriceText.setEnabled(true);
					percentText.setEnabled(false);
					
					limitPriceText.setEditable(false);
					stopPriceText.setEnabled(true);
					percentText.setEnabled(false);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					
				}
				
				else if(orderType.equalsIgnoreCase(OrderType.STOPLIMIT.name())) {
					limitPriceText.setEnabled(true);
					stopPriceText.setEnabled(true);
					percentText.setEnabled(false);
					
					limitPriceText.setEditable(true);
					stopPriceText.setEnabled(true);
					percentText.setEnabled(false);
					
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					
				}
				
				else if(orderType.equalsIgnoreCase(OrderType.TRAILLING_STOP.name())) {
					limitPriceText.setEnabled(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(true);
					
					limitPriceText.setEditable(true);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(true);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
				}
				
				else if(orderType.equalsIgnoreCase(OrderType.LIMIT_TRAILLING_STOP.name())) {
					limitPriceText.setEnabled(true);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(true);
					
					limitPriceText.setEditable(true);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(true);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
				}
				
				
				else if(orderType.equalsIgnoreCase(OrderType.MARKET.name())) {
					limitPriceText.setEnabled(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					
					limitPriceText.setEditable(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
				}
				
			}

		});
		
		
		Label isExecutionTimeLabel = new Label(topComposite, SWT.NONE);
		isExecutionTimeLabel.setText("Exe Order Time             ");
		isExecutionTimeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		isExecutionOrderCheckBox = new Button(topComposite, SWT.CHECK);
		isExecutionOrderCheckBox.setEnabled(false);
		
		Label timeLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		timeLabel.setText("Time   ");
		timeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
        time = new DateTime(topComposite, SWT.TIME);;
        time.setEnabled(false);
		//time.setRegion(region);
		isExecutionOrderCheckBox.addSelectionListener(new SelectionAdapter() {
		    @Override
		    public void widgetSelected(SelectionEvent event) {
		    	Button btn = (Button) event.getSource();
		    	isExecition = btn.getSelection();
		    	if(isExecition) {
		    		time.setEnabled(true);
		    	}else {
		    		time.setEnabled(false);
		    	}
		    }
		});
		
		time.addSelectionListener(new SelectionAdapter() {
			 @Override
			    public void widgetSelected(SelectionEvent event) {
				  int hrs = ((DateTime)event.getSource()).getHours();
				  int minutes = ((DateTime)event.getSource()).getMinutes();
				  int seconds = ((DateTime)event.getSource()).getSeconds();
			    	Date date = createDate();
			    	date = addHoursToDate(date, hrs);
					date = addMinutesToDate(date, minutes);
					date = addMinutesToDate(date, seconds);
				    localDateTime = String.format("%tY-%<tm-%<tdT%<tH:%<tM:%<tS", date);
			    }
		});
		Composite sideButtonComposite = new Composite(parent, SWT.NONE);
		GridLayout sidetopCompositeLayout = new GridLayout(2, false);
		sideButtonComposite.setLayout(sidetopCompositeLayout);
		sideButtonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		buyButton = new Button(sideButtonComposite, SWT.PUSH);
		buyButton.setSize(100, 25);
		sellButton = new Button(sideButtonComposite, SWT.PUSH);
		sellButton.setSize(1000, 25);
		buyButton.setText("                                              BUY                                         ");
		buyButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		buyButton.setBackground(black);
		buyButton.addSelectionListener(getButtonSelectionAdapter1());

		sellButton.setText("                                              SELL                                         ");
		sellButton.setBackground(black);
		sellButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		sellButton.addSelectionListener(getButtonSelectionAdapter1());
		
		//addButtonComposite(topComposite); //TODO:  Get order Type and order type values based on type selected Button clicked i.e limit or stop or trailstop and those associated values
		
		//place order
		placeOrderButton = new Button(parent, SWT.PUSH);
		placeOrderButton.setText("                                                                                                       Place Order                                                                                      ");
		placeOrderButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		placeOrderButton.setBackground(greenColor);
		placeOrderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
//				int callService = confirmServiceCall(parent);
//				
//				if(callService == 256) {	
//					return;
//				}
				placeOrderButton.addKeyListener(new KeyListener() {
					
					@Override
					public void keyReleased(KeyEvent e) {
						
					}
					
					@Override
					public void keyPressed(KeyEvent e) {
						placeOrderButton.setEnabled(false);
					}
				});
				//parent.requestLayout();
				String side = buyOrSell;// BUY or SELL
				String orderType =  orderTypeSelected;// stop, stoplimit, trailingstop, trailingstopprice, limittrailstop
				String limitPrice =  limitPriceText.getText();// 
				String stopPrice = stopPriceText.getText();//
				String percentage = percentText.getText();// trailpercent
				if(tradeOrSnipe.equalsIgnoreCase("Trade")) {
					callCreateOrderService(fromAddress.getText(), 
							toAddressText.getText(), 
						    inputAmountText.getText(),
						    slipagelabelText.getText(),
						    gasModeComboitems.getText(), 
						    gasGweiText.getText(),
						    gasLimitText.getText(),
						    side,
						    orderType,
						    limitPrice,
						    stopPrice,
						    percentage, 
						    dexRoute,
						    isFeeEligibile
						    );
				}else {
					callSnipeOrderService(fromAddress.getText(), 
										  toAddressText.getText(), 
									      inputAmountText.getText(),
									      slipagelabelText.getText(),
									      gasModeComboitems.getText(), 
									      gasGweiText.getText(),
									      gasLimitText.getText(),
									      side,
									      orderType,
									      limitPrice,
									      stopPrice,
									      percentage, 
									      routeComboitems.getText(), 
									      isFeeEligibile, 
									      localDateTime);
				}
				placeOrderButton.setEnabled(true);	
			}
		});
		parent1.pack();
	}
	
	private void callCreateOrderService(String fromAddress, String toAddress, String amount, 
										String slipage, String gasMode, String gasGwei, 
										String gasLimitGwei, String side,  
										String orderType, String limitPrice, 
										String stopPrice, String percentage, String route, boolean isFeeEligibile) {
		try {
			OrderRequestPreparer  orderRequestPreparer = new OrderRequestPreparer();
			Order order = prepareOrder(fromAddress, toAddress, amount, slipage, gasMode, gasGwei, gasLimitGwei, side, orderType,
									   limitPrice, stopPrice, percentage, route, isFeeEligibile, orderRequestPreparer);
			
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("order json "+mapper.writeValueAsString(order));
			callOrderService(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Order prepareOrder(String fromAddress, String toAddress, String amount, String slipage, String gasMode,
			String gasGwei, String gasLimitGwei, String side, String orderType, String limitPrice, String stopPrice,
			String percentage, String route, boolean isFeeEligibile, OrderRequestPreparer orderRequestPreparer) {
		return orderRequestPreparer.createOrder(fromAddress, toAddress, amount, slipage, gasMode, gasGwei, gasLimitGwei, 
				side, orderType, limitPrice, stopPrice, percentage, route, isFeeEligibile);
	}

	private void callOrderService(Order order) {
		HttpEntity<Order> httpEntity = new HttpEntity<Order>(order,createSecurityHeaders());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseEntity =  restTemplate.exchange(CREATE_ORDER, HttpMethod.POST, httpEntity, String.class);
		Object respose =  responseEntity.getBody();
		System.out.println("sucess " + respose);
	}
	
	private void callSnipeOrderService(String fromAddress, String toAddress, String amount, String slipage,
									   String gasMode, String gasGwei, String gasLimitGwei, String orderType,  
									   String side, String limitPrice, String stopPrice, 
									   String percentage, String route, boolean isFeeEligibile, String localDateTime) {
		try {
			SnipeRequestPreparer  snipeRequestPreparer = new SnipeRequestPreparer();
			SnipeTransactionRequest snipeTransactionRequest = snipeRequestPreparer.createSnipeTransactionRequest(fromAddress, toAddress, amount, slipage, 
					gasMode, gasGwei, gasLimitGwei, orderType, side, limitPrice, stopPrice, percentage, route, isFeeEligibile, localDateTime);
			snipeTransactionRequest.setExeTimeCheck(isExecition);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("order json "+mapper.writeValueAsString(snipeTransactionRequest));
			// here we need to do profit take order;
			
			HttpEntity<SnipeTransactionRequest> httpEntity = new HttpEntity<SnipeTransactionRequest>(snipeTransactionRequest,createSecurityHeaders());
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity =  restTemplate.exchange(SNIPE_ORDER, HttpMethod.POST, httpEntity, String.class);
			Object respose =  responseEntity.getBody();
			System.out.println("sucess " +respose);
			if(takeProfitOrderLimit.getText() != null && !takeProfitOrderLimit.getText().isEmpty()) {
				String snipeOrderId = (String)respose;
				
				Order order = prepareOrder(snipeTransactionRequest.getToAddress(), toAddress, amount, "5", gasMode, gasGwei, gasLimitGwei, "SELL", "MARKET",
						   				   limitPrice, stopPrice, percentage, route, isFeeEligibile, new OrderRequestPreparer());
				order.setParentSnipeId(snipeOrderId);
				order.setAutoSnipeLimitSellTrailPercent(takeProfitOrderLimit.getText());
				callOrderService(order);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private MultiValueMap<String, String> createSecurityHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("public-key", "publickey");
		httpHeaders.set("token", "securityToken");
		httpHeaders.set("auth-source-sys", "LDAP");
		return httpHeaders;
	}

	/**
	 * confirmServiceCall
	 * 
	 * @param parent
	 * @return
	 */
	private static int confirmServiceCall(Composite parent) {
		Shell shell = Display.getDefault().getActiveShell();
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Trade");
		dialog.setMessage("Please check DEX, From & To for Buy & Sell!!");
		return dialog.open();
	}
	
	private  SelectionAdapter getButtonSelectionAdapter1() {
		 
		SelectionAdapter sAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selButton = ((Button)e.getSource()).getText().trim();
				buyOrSell =	selButton;
				
				if(e.getSource()!= null && "BUY".equalsIgnoreCase(selButton)) {
					buyButton.setBackground(lightBlueColor);
					sellButton.setBackground(black);
					fromAddress.setMessage("ETH/WETH/BNB defaulted based on DEX ROUTE-BUY");
					toAddressText.setMessage("");
					
				} else {
					sellButton.setBackground(redColor);
					buyButton.setBackground(black);
					fromAddress.setMessage("");
					toAddressText.setMessage("ETH/WETH/BNB defaulted based on DEX ROUTE-SELL");
				}
			}};
		return sAdapter;
	
	}
	
	private  SelectionAdapter getButtonSelectionAdapter() {
		 
		SelectionAdapter sAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selButton = ((Button)e.getSource()).getText().trim();
				tradeOrSnipe =	selButton;
				if(e.getSource()!= null && "SNIPE".equalsIgnoreCase(selButton)) {
					snipeButton.setBackground(lightGreenColor);
					tradeButton.setBackground(black);
					fromAddress.setText("PANCAKE".equalsIgnoreCase(dexRoute) ? BNB_ADDRESS : ETH_ADDRESS);
					fromAddress.setEditable(false);
					fromAddress.setEnabled(false);
					limitPriceText.setEnabled(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					toAddressText.setMessage("");
					limitPriceText.setEditable(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
					orderTypeLabelComboitems.setEnabled(false);
					orderTypeLabelComboitems.setEnabled(false);
					buyButton.setEnabled(false);
					sellButton.setEnabled(false);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					orderTypeLabelComboitems.setBackground(device.getSystemColor(SWT.COLOR_DARK_GRAY));
					
					sellButton.setBackground(black);
					buyButton.setBackground(black);
					isExecutionOrderCheckBox.setEnabled(true);
					takeProfitOrderLimit.setEnabled(true);
					takeProfitOrderLimit.setEditable(true);
					if("PANCAKE".equalsIgnoreCase(dexRoute)) {
						gasGweiText.setEnabled(true);
						gasLimitText.setEditable(true);
						gasGweiText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
						gasLimitText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					}
				} else {
					fromAddress.setText("");
					tradeButton.setBackground(lightGreenColor);
					snipeButton.setBackground(black);
					orderTypeLabelComboitems.setEnabled(true);
					orderTypeLabelComboitems.setEnabled(true);
					fromAddress.setEditable(true);
					fromAddress.setEnabled(true);
					buyButton.setEnabled(true);
					sellButton.setEnabled(true);
					
					limitPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					stopPriceText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					percentText.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					orderTypeLabelComboitems.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
					sellButton.setBackground(greyColor);
					buyButton.setBackground(greyColor);
					isExecutionOrderCheckBox.setEnabled(false);
					time.setEnabled(false);
					
					takeProfitOrderLimit.setEnabled(false);
					takeProfitOrderLimit.setEditable(false);
				}
			}};
		return sAdapter;
	
	}
	
	private static Date createDate() {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date addSecondsToDate(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, minutes);
		return calendar.getTime();
	}
	
	public static Date addMinutesToDate(Date date, int minutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minutes);
		return calendar.getTime();
	}

	public static Date addHoursToDate(Date date, int hours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}
}