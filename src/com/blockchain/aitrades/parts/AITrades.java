package com.blockchain.aitrades.parts;

import javax.annotation.PostConstruct;

import org.eclipse.osgi.internal.debug.FrameworkDebugOptions;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.blockchain.aitrades.domain.Order;
import com.blockchain.aitrades.domain.OrderType;
import com.blockchain.aitrades.domain.SnipeTransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AITrades {
	
	private static final String CUSTOM = "CUSTOM";
	private static final String CREATE_ORDER = "http://localhost:8080/aitrades/eth-gateway/order/api/v1/createOrder";
	private static final String SNIPE_ORDER = "http://localhost:8080/aitrades/eth-gateway/snipe/api/v1/snipeOrder";
	private static final String ETH_ADDRESS = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";

	private Button tradeButton = null;
	private Button snipeButton = null;
	private String tradeOrSnipe=null;
	
	private Button buyButton = null;
	private Button sellButton = null;
	private String buyOrSell=null;
	
	private String orderTypeSelected = null;
	private String dexRoute = "UNISWAP";
	
	//snipe order : 
	Text fromAddress = null;
	Text toAddressText = null;
	Text limitPriceText = null;
	Text stopPriceText = null;
	Text percentText = null;
	Combo orderTypeLabelComboitems = null;
	
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

		
		routeComboitems.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dexRoute = ((Combo)e.getSource()).toString();
			}
		});
		
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
		
		
		Label gasModeLabel = new Label(topComposite, SWT.NONE);
		gasModeLabel.setText("GAS MODE           ");
		gasModeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		Combo gasModeComboitems = new Combo(topComposite, SWT.DROP_DOWN);
		String[] gasModetems = new String[] { "ULTRA", "FASTEST", "FAST", "STANDARD", "SAFELOW", CUSTOM};
		gasModeComboitems.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		gasModeComboitems.setItems(gasModetems);
		gasModeComboitems.select(1);
		gasModeComboitems.pack();
		
		
		Label gasMinLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		gasMinLabel.setText("GAS PRICE           ");
		Text gasGweiText = new Text(topComposite, SWT.NONE);
		gasGweiText.setLayoutData(new GridData(100, 20));
		gasMinLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		Label gasLimitLabel = new Label(topComposite, SWT.NONE);// if gas mode is custom then enable gasprice
		gasLimitLabel.setText("GAS LIMIT           ");
		Text gasLimitText = new Text(topComposite, SWT.NONE);
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
		Button placeOrderButton = new Button(parent, SWT.PUSH);
		placeOrderButton.setText("                                                                                                       Place Order                                                                                      ");
		placeOrderButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		placeOrderButton.setBackground(greenColor);
		placeOrderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int callService = confirmServiceCall(parent);
				
				if(callService == 256) {
					return;
				}

				parent.requestLayout();
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
						    dexRoute
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
									      routeComboitems.getText());
				}
			}
		});
		parent1.pack();
	}
	
	private void callCreateOrderService(String fromAddress, String toAddress, String amount, 
										  String slipage, String gasMode, String gasGwei, 
										  String gasLimitGwei, String side,  
										 String orderType, String limitPrice, 
										 String stopPrice, String percentage, String route) {
		try {
			OrderRequestPreparer  orderRequestPreparer = new OrderRequestPreparer();
			Order order = orderRequestPreparer.createOrder(fromAddress, toAddress, amount, slipage, gasMode, gasGwei, gasLimitGwei, 
					side, orderType, limitPrice, stopPrice, percentage, route);
			
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("order json "+mapper.writeValueAsString(order));
			
			HttpEntity<Order> httpEntity = new HttpEntity<Order>(order,createSecurityHeaders());
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity =  restTemplate.exchange(CREATE_ORDER, HttpMethod.POST, httpEntity, String.class);
			Object respose =  responseEntity.getBody();
			System.out.println("sucess " + respose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void callSnipeOrderService(String fromAddress, String toAddress, String amount, String slipage,
									   String gasMode, String gasGwei, String gasLimitGwei, String orderType,  
									   String side, String limitPrice, String stopPrice, 
									   String percentage, String route) {
		try {
			SnipeRequestPreparer  snipeRequestPreparer = new SnipeRequestPreparer();
			SnipeTransactionRequest snipeTransactionRequest = snipeRequestPreparer.createSnipeTransactionRequest(fromAddress, toAddress, amount, slipage, 
					gasMode, gasGwei, gasLimitGwei, orderType, side, limitPrice, stopPrice, percentage, route);
			HttpEntity<SnipeTransactionRequest> httpEntity = new HttpEntity<SnipeTransactionRequest>(snipeTransactionRequest,createSecurityHeaders());
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity =  restTemplate.exchange(SNIPE_ORDER, HttpMethod.POST, httpEntity, String.class);
			Object respose =  responseEntity.getBody();
			System.out.println("sucess " +respose);
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
		dialog.setMessage("Do you want to call trade?");
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
					fromAddress.setText(ETH_ADDRESS);
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
				}
			}};
		return sAdapter;
	
	}
}