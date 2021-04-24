package com.blockchain.aitrades.parts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.aitrades.blockchain.eth.gateway.clients.OrderHistroyRetrieverClient;
import com.aitrades.blockchain.eth.gateway.clients.RetriggerSnipeOrderClient;
import com.aitrades.blockchain.eth.gateway.domain.Convert;
import com.aitrades.blockchain.eth.gateway.domain.Order;
import com.aitrades.blockchain.eth.gateway.domain.OrderHistory;
import com.aitrades.blockchain.eth.gateway.domain.OrderType;
import com.aitrades.blockchain.eth.gateway.domain.SnipeTransactionRequest;
import com.aitrades.blockchain.eth.gateway.request.OrderRequestPreparer;
import com.aitrades.blockchain.eth.gateway.request.SnipeRequestPreparer;

public class AITrades {
	
	private static final String CUSTOM = "CUSTOM";
	private static final String CREATE_ORDER = "http://localhost:8080/aitrades/eth-gateway/order/api/v1/createOrder";
	private static final String SNIPE_ORDER = "http://localhost:8080/aitrades/eth-gateway/snipe/api/v1/snipeOrder";
	
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
	Text slipagelabelText = null;
	//snipe order : 
	Text contractInteraction = null;
	Text inputAmountText = null;
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
	
	
	Text minLiquidityText = null;
	Text expectedTokensText = null;
	
	
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
	
	TableViewer histroyTableViewer = null;
	Button refreshButton= null;
	RGB rgbWhite = new RGB(255, 255, 255);
	Color whiteColor = new Color(device, rgbWhite);
	
	String localDateTime =  null;
	String localDateTime1 =  null;
	private String ethWalletPublicKey = "0x7B74B57c89A73145Fe1915f45d8c23682fF78341";
	private String bscWalletPublicKey = "0xF007afdB97c3744762F953C07CD45Dd237663C3F";
	
	private Text retriggerSlipage = null;
	private Text retriggerGasPrice = null;
	private Text retriggerGasLimit = null;
	
	@PostConstruct
	public void createComposite(Composite parent1) {
		
		parent1.setLayout(new GridLayout(1, true));
		parent1.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent1.setLayoutData(data);
		data.minimumHeight=100;
		
		Composite timeComposite = new Composite(parent1, SWT.NONE);
		timeComposite.setLayout(new GridLayout(2, true));
		timeComposite.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		GridData data1 = new GridData(SWT.FILL, SWT.FILL, true, true);
		timeComposite.setLayoutData(data1);
		
		Display display = Display.getDefault();
			Label UTCTimeLabel = new Label(timeComposite, SWT.NONE);
			UTCTimeLabel.setText("UTC Time: ");
			UTCTimeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
			Text utcTimeText = new Text(timeComposite, SWT.BOLD);
			utcTimeText.setBackground(device.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			utcTimeText.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
			utcTimeText.setLayoutData(new GridData(300, 20));
			utcTimeText.setEditable(false);
			utcTimeText.setEnabled(false);
			utcTimeText.setTextDirection(SWT.AUTO_TEXT_DIRECTION);
		    
			Label currentTimeLabel = new Label(timeComposite, SWT.NONE);
			currentTimeLabel.setText("Current Time: ");
			currentTimeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
			Text currentTimeText = new Text(timeComposite, SWT.BOLD);
			currentTimeText.setLayoutData(new GridData(300, 20));
			currentTimeText.setEditable(false);
			currentTimeText.setTextDirection(SWT.AUTO_TEXT_DIRECTION);
			currentTimeText.setEnabled(false);
			currentTimeText.setBackground(device.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			currentTimeText.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
			
			
			display.timerExec(1000, new Runnable() {
				@Override
				public void run() {
					currentTimeText.setText(Instant.now().atZone(ZoneId.systemDefault()).toString());
					utcTimeText.setText(Instant.now().toString());
					 display.timerExec( 1000, this );
				}
			});
			
			
			
		
		Composite parent = new Composite(parent1, SWT.NONE);
		parent.setLayout(new GridLayout(2, true));
		parent.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		parent.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));
		
		Composite orderHistoryParent = new Composite(parent1, SWT.FILL);
		orderHistoryParent.setLayout(new GridLayout(16, true));
		orderHistoryParent.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		//orderHistoryParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		histroyTableViewer = new TableViewer(parent1, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		histroyTableViewer.setContentProvider(new ArrayContentProvider());
		createColumns(orderHistoryParent, histroyTableViewer);
		final Table table = histroyTableViewer.getTable();
		
	    table.setHeaderVisible(true);
	    GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
	    table.setLayoutData(gd_table);
	    table.setLinesVisible(true);
	    table.setHeaderBackground(device.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
	    table.setSize(100, 100);
	    histroyTableViewer.setContentProvider(new ArrayContentProvider());

		GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight=50;
        
        histroyTableViewer.getControl().setLayoutData(gridData);
		refreshButton= new Button(orderHistoryParent, SWT.PUSH);
	    refreshButton.setText("Refresh");
	    refreshButton.setForeground(lightBlueColor);
	    refreshButton.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
	    refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OrderHistroyRetrieverClient  histroyRetriever = new OrderHistroyRetrieverClient();
				List<OrderHistory> retrieveOrderHistroy = histroyRetriever.retrieveOrderHistroy(ethWalletPublicKey, bscWalletPublicKey);
				histroyTableViewer.setInput(retrieveOrderHistroy);
				histroyTableViewer.refresh();
			}
		});
//	    
//	    display.timerExec(60000, new Runnable() {
//			@Override
//			public void run() {
//				OrderHistroyRetriever  histroyRetriever = new OrderHistroyRetriever();
//				histroyTableViewer.setInput(histroyRetriever.retrieveOrderHistroy(ethWalletPublicKey, bscWalletPublicKey));
//				histroyTableViewer.refresh();
//				display.timerExec( 1000, this );
//			}
//		});
		Composite orderHistoryParent1 = new Composite(parent1, SWT.FILL);
		orderHistoryParent1.setLayout(new GridLayout(1, true));
		orderHistoryParent1.setBackground(device.getSystemColor(SWT.COLOR_BLACK));
		Composite tradeParent = new Composite(parent, SWT.NONE);
		
		tradeParent.setLayout(new GridLayout(1, true));
		tradeParent.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));

		Composite topComposite = new Composite(tradeParent, SWT.BORDER);
		topComposite.setLayout(new GridLayout(2, true));
		GridData topGriddate = new GridData(SWT.NONE, SWT.NONE, true, true);
		topComposite.setLayoutData(topGriddate);

		Composite topBtnCompositetrade = new Composite(topComposite, SWT.NONE);
		topBtnCompositetrade.setLayout( new GridLayout(1, true));
		topBtnCompositetrade.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));
		
		Composite topBtnCompositesell = new Composite(topComposite, SWT.NONE);
		topBtnCompositesell.setLayout( new GridLayout(1, true));
		topBtnCompositesell.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true, true));
		
		tradeButton = new Button(topBtnCompositetrade, SWT.PUSH);
		snipeButton = new Button(topBtnCompositesell, SWT.PUSH);
	
		tradeButton.setText("                                              TRADE                                        ");
		tradeButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		tradeButton.setBackground(black);
		tradeButton.addSelectionListener(getButtonSelectionAdapter());
		
		snipeButton.setText("                                              SNIPE                                         ");
		snipeButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		snipeButton.setBackground(black);
		snipeButton.addSelectionListener(getButtonSelectionAdapter());
		
		Label routeLabel = new Label(topComposite, SWT.NONE);
		routeLabel.setText("DEX                 ");
		routeLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		Combo routeComboitems = new Combo(topComposite, SWT.DROP_DOWN);
		String[] routetems = new String[] { "UNISWAP", "SUSHI", "PANCAKE"};
		routeComboitems.setItems(routetems);
		routeComboitems.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
		routeComboitems.select(0);
		routeComboitems.pack();
		
		
		Label inputAmountLabel = new Label(topComposite, SWT.NONE);
		inputAmountLabel.setText("INPUT AMOUNT   ");
		inputAmountLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		inputAmountText = new Text(topComposite, SWT.NONE);
		inputAmountText.setLayoutData(new GridData(100, 20));
		
		Label fromLabel = new Label(topComposite, SWT.NONE);
		fromLabel.setText("Contract Address");
		fromLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		contractInteraction = new Text(topComposite, SWT.NONE);
		contractInteraction.setLayoutData(new GridData(300, 20));
		

		
		//Slippage combo
		Label slipagelabel = new Label(topComposite, SWT.NONE);
		slipagelabel.setText("SLIPAGE            ");
		slipagelabelText = new Text(topComposite, SWT.NONE);
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
				gasModeComboitems.setEnabled(true);
				gasLimitText.setEditable(true);
				gasGweiText.setEnabled(true);
				gasGweiText.setEditable(true);
				gasLimitText.setEditable(true);
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
		
		
		Label minLiquidityLabel = new Label(topComposite, SWT.NONE);
		minLiquidityLabel.setText("Min Liquidity");
		minLiquidityLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		minLiquidityText = new Text(topComposite, SWT.NONE);
		minLiquidityText.setLayoutData(new GridData(100, 20));
		minLiquidityText.setEnabled(false);
		minLiquidityText.setEditable(false);
		
		Label expectedTokensLabel = new Label(topComposite, SWT.NONE);
		expectedTokensLabel.setText("IDO Expected Token");
		expectedTokensLabel.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		
		expectedTokensText = new Text(topComposite, SWT.NONE);
		expectedTokensText.setLayoutData(new GridData(100, 20));
		expectedTokensText.setEnabled(false);
		expectedTokensText.setEditable(false);
		
		Composite sideButtonComposite = new Composite(topComposite, SWT.NONE);
		GridLayout sidetopCompositeLayout = new GridLayout(1, false);
		sideButtonComposite.setLayout(sidetopCompositeLayout);
		sideButtonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		Composite sideButtonComposite1 = new Composite(topComposite, SWT.NONE);
		GridLayout sidetopCompositeLayout1 = new GridLayout(1, false);
		sideButtonComposite1.setLayout(sidetopCompositeLayout1);
		sideButtonComposite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
		buyButton = new Button(sideButtonComposite, SWT.PUSH);
		buyButton.setSize(100, 25);
		sellButton = new Button(sideButtonComposite1, SWT.PUSH);
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
		Composite palceOrderComposite = new Composite(tradeParent, SWT.NONE);
		GridLayout palceOrderCompositeLayout = new GridLayout(1, false);
		palceOrderComposite.setLayout(palceOrderCompositeLayout);
		palceOrderComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//place order
		placeOrderButton = new Button(palceOrderComposite, SWT.PUSH);
		placeOrderButton.setText("                                                                                                       Place Order                                                                                      ");
		placeOrderButton.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
		placeOrderButton.setBackground(greenColor);
		placeOrderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int callService = confirmServiceCall(parent, "Please check DEX, From & To for Buy & Sell!!");
				
				if(callService == 256) {	
					return;
				}
				placeOrderButton.addKeyListener(new KeyListener() {
					
					@Override
					public void keyReleased(KeyEvent e) {
					}
					
					@Override
					public void keyPressed(KeyEvent e) {
					}
				});
				//parent.requestLayout();
				String side = buyOrSell;// BUY or SELL
				String orderType =  orderTypeSelected;// stop, stoplimit, trailingstop, trailingstopprice, limittrailstop
				String limitPrice =  limitPriceText.getText();// 
				String stopPrice = stopPriceText.getText();//
				String percentage = percentText.getText();// trailpercent
				if(tradeOrSnipe.equalsIgnoreCase("Trade")) {
					callCreateOrderService(contractInteraction.getText().trim(), 
							"", 
						    inputAmountText.getText().trim(),
						    slipagelabelText.getText().trim(),
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
					callSnipeOrderService(contractInteraction.getText().trim(), 
										  "", 
									      inputAmountText.getText().trim(),
									      slipagelabelText.getText().trim(),
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
			}
		});
		parent1.pack();
	}
	
	private void createColumns(Composite orderHistoryParent, TableViewer histroyTableViewer) {
        TableViewerColumn col = createTableViewerColumn("Order Id", 100, 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getOrderId();
            }
        });
        
        col = createTableViewerColumn("Route", 100, 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getRoute();
            }
        });
        
        col =createTableViewerColumn("Trade", 100, 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getTradetype();
            }
        });
        
        col =createTableViewerColumn("From Tkr", 100, 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getFromTickerSymbol();
            }
        });
        
        col =createTableViewerColumn("Amount", 100, 4);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getInput();
            }
        });
        
        col =createTableViewerColumn("To Tkr", 100, 5);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getToTickerSymbol();
            }
        });
        
        col =createTableViewerColumn("Amount", 100, 6);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getOutput();
            }
        });
        
        col =createTableViewerColumn("Executed Price", 100, 7);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getExecutedprice();
            }
        });
        
        col =createTableViewerColumn("Order State", 100, 8);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getOrderstate();
            }
        });
        
        col =createTableViewerColumn("Approved Status", 100, 9);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getApprovedhashStatus() != null && !p.getApprovedhashStatus().isEmpty() ?  p.getApprovedhashStatus() : p.getApprovedhash();
            }
        });
        col =createTableViewerColumn("Swap Status", 100, 10);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getSwappedhashStatus() != null && !p.getSwappedhashStatus().isEmpty() ?  p.getSwappedhashStatus() : p.getSwappedhash();
            }
        });

        col =createTableViewerColumn("Order Side", 100, 11);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getOrderside();
            }
        });
        col =createTableViewerColumn("Error Msg", 100, 12);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                OrderHistory p = (OrderHistory) element;
                return p.getErrormessage();
            }
        });
        
        col =createTableViewerColumn("Slipage", 100, 13);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	 OrderHistory p = (OrderHistory) element;
                 return p.getSlipage();
            }
        });
        col.setEditingSupport(new EditingSupport(histroyTableViewer) {
        	CellEditor editor = new TextCellEditor(histroyTableViewer.getTable());
        	
        	@Override
    	    protected Object getValue(Object element) {
        		if(element != null) {
        			return ((OrderHistory) element).getSlipage();
        		}
        		return null;
    	    }

    	    @Override
    	    protected void setValue(Object element, Object userInputValue) {
    	    	((OrderHistory) element).setSlipage(String.valueOf(userInputValue));
    	    	histroyTableViewer.update(element, null);
    	    }
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
        
        col =createTableViewerColumn("Gas Price", 100, 14);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	OrderHistory p = (OrderHistory) element;
                return p.getGasPrice();
            }
        });
        col.setEditingSupport(new EditingSupport(histroyTableViewer) {
        	CellEditor editor = new TextCellEditor(histroyTableViewer.getTable());
        	
        	@Override
    	    protected Object getValue(Object element) {
        		if(element != null) {
        			return ((OrderHistory) element).getGasPrice();
        		}
        		return null;
    	    }

    	    @Override
    	    protected void setValue(Object element, Object userInputValue) {
    	    	System.out.println("gas price"+userInputValue);
    	    	((OrderHistory) element).setGasPrice(String.valueOf(userInputValue));
    	    	histroyTableViewer.update(element, null);
    	    }
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
        col =createTableViewerColumn("Gas Limit", 100, 15);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
            	OrderHistory p = (OrderHistory) element;
                return p.getGasLimit();
            }
            
        });
        col.setEditingSupport(new EditingSupport(histroyTableViewer) {
        	CellEditor editor = new TextCellEditor(histroyTableViewer.getTable());
        	
        	@Override
    	    protected Object getValue(Object element) {
        		if(element != null) {
        			return ((OrderHistory) element).getGasLimit();
        		}
        		return null;
    	    }

    	    @Override
    	    protected void setValue(Object element, Object userInputValue) {
    	    	System.out.println("gas limit"+userInputValue);
    	    	((OrderHistory) element).setGasLimit(String.valueOf(userInputValue));
    	    	histroyTableViewer.update(element, null);
    	    }
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
        
        col =createTableViewerColumn("Retrigger", 100, 16);
        col.setLabelProvider(new ColumnLabelProvider() {
            //make sure you dispose these buttons when viewer input changes
            @Override
            public void update(ViewerCell cell) {

                TableItem item = (TableItem) cell.getItem();
                OrderHistory history = (OrderHistory)item.getData();
					if(history.getOrderId() != null && !history.getOrderId().isEmpty()) {
						Button button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
						button.setText("Retrigger");
						button.setForeground(device.getSystemColor(SWT.COLOR_WHITE));
						button.setBackground(greenColor);
						TableEditor editor = new TableEditor(item.getParent());
						editor.grabHorizontal = true;
						editor.grabVertical = true;
						editor.setEditor(button, item, cell.getColumnIndex());
						editor.layout();
						button.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								RetriggerSnipeOrderClient retriggerSnipeOrderClient = new RetriggerSnipeOrderClient();
								String response = retriggerSnipeOrderClient.retriggerSnipeOrder(history.getOrderId(),
										history.getSlipage(), history.getGasPrice(),
										history.getGasLimit());// (String id, String slipage, String gasPrice, String gasLimit)
								if (response != null && !response.isEmpty()) {
									OrderHistroyRetrieverClient  histroyRetriever = new OrderHistroyRetrieverClient();
									histroyTableViewer.setInput(histroyRetriever.retrieveOrderHistroy(ethWalletPublicKey, bscWalletPublicKey));
									histroyTableViewer.refresh();
								}
							}
						});
					}
            }});
        
        
        col =createTableViewerColumn("Updated Datetime", 100, 17);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return LocalDateTime.now().toString();
            }
        });
        
	}

	 private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	        final TableViewerColumn viewerColumn = new TableViewerColumn(histroyTableViewer, SWT.NONE);
	        final TableColumn column = viewerColumn.getColumn();
	        column.setText(title);
	        column.setWidth(bound);
	        column.setResizable(true);
	      //  column.addSelectionListener(getSelectionAdapter(column, colNumber));
	        return viewerColumn;
	    }
	 private SelectionAdapter getSelectionAdapter(final TableColumn column,
	            final int index) {
	        SelectionAdapter selectionAdapter = new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	            }
	        };
	        return selectionAdapter;
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
			callOrderService(order);
			clearValues();
		} catch (Exception e) {
			e.printStackTrace();
			clearValues();
		}finally {
			clearValues();
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
		System.out.println("Order Id " + respose);
	}
	
	private void callSnipeOrderService(String fromAddress, String toAddress, String amount, String slipage,
									   String gasMode, String gasGwei, String gasLimitGwei, String orderType,  
									   String side, String limitPrice, String stopPrice, 
									   String percentage, String route, boolean isFeeEligibile, String localDateTime) {
		RestTemplate restTemplate = null;
		try {
			SnipeRequestPreparer  snipeRequestPreparer = new SnipeRequestPreparer();
			SnipeTransactionRequest snipeTransactionRequest = snipeRequestPreparer.createSnipeTransactionRequest(fromAddress, toAddress, amount, slipage, 
					gasMode, gasGwei, gasLimitGwei, orderType, side, limitPrice, stopPrice, percentage, route, isFeeEligibile, localDateTime);
			snipeTransactionRequest.setExeTimeCheck(isExecition);
			if(minLiquidityText != null && minLiquidityText.getText() != null && !minLiquidityText.getText().isEmpty()) {
				snipeTransactionRequest.setLiquidityQuantity(Convert.toWei(minLiquidityText.getText(), Convert.Unit.GWEI).toBigInteger());
			}
			if(expectedTokensText != null && expectedTokensText.getText() != null && !expectedTokensText.getText().isEmpty()) {
				snipeTransactionRequest.setExpectedOutPutToken(Convert.toWei(expectedTokensText.getText(), Convert.Unit.GWEI).toBigInteger());
			}
			HttpEntity<SnipeTransactionRequest> httpEntity = new HttpEntity<SnipeTransactionRequest>(snipeTransactionRequest,createSecurityHeaders());
			restTemplate = new RestTemplate();
			ResponseEntity<String> responseEntity =  restTemplate.exchange(SNIPE_ORDER, HttpMethod.POST, httpEntity, String.class);
			Object respose =  responseEntity.getBody();
			clearValues();
			System.out.println("Snipe Order Id:  " +respose);
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
			clearValues();
		}finally {
			restTemplate = null;
			clearValues();
		}
	}
	

	private void clearValues() {
		contractInteraction.setText("");
		inputAmountText.setText("");
		gasLimitText.setText("");
		gasGweiText.setText("");
		slipagelabelText.setText("");
		minLiquidityText.setText("");
		expectedTokensText.setText("");
		takeProfitOrderLimit.setText("");
	}

	private BigDecimal buildExpectedOutPutAmount(Text preListingSalePricetxt, Text expectedToleranceTimestxt, String inputAmount) {
		BigDecimal preListingSalePrice = new BigDecimal(preListingSalePricetxt.getText()).setScale(8, RoundingMode.DOWN);
		BigDecimal expectedToleranceTimes = new BigDecimal(expectedToleranceTimestxt.getText()).setScale(0, RoundingMode.DOWN);;
		BigDecimal amountInBigDec = new BigDecimal(inputAmount).setScale(4, RoundingMode.DOWN);
		
		BigDecimal maxPriceTolerance = (preListingSalePrice.multiply(expectedToleranceTimes)).setScale(2, RoundingMode.DOWN);
		
		double result =  amountInBigDec.doubleValue() / maxPriceTolerance.doubleValue();
		
		return BigDecimal.valueOf(result).setScale(4, RoundingMode.DOWN);
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
	private static int confirmServiceCall(Composite parent, String message) {
		Shell shell = Display.getDefault().getActiveShell();
		MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("Trade");
		dialog.setMessage(message);
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
				} else {
					sellButton.setBackground(redColor);
					buyButton.setBackground(black);
				}
				minLiquidityText.setEditable(false);
				minLiquidityText.setEnabled(false);
				
				expectedTokensText.setEditable(false);
				expectedTokensText.setEnabled(false);
				
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
					limitPriceText.setEnabled(false);
					stopPriceText.setEnabled(false);
					percentText.setEnabled(false);
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
					gasGweiText.setEnabled(true);
					gasLimitText.setEditable(true);
					minLiquidityText.setEditable(true);
					minLiquidityText.setEnabled(true);
					expectedTokensText.setEditable(true);
					expectedTokensText.setEnabled(true);
					
				} else {
					tradeButton.setBackground(lightGreenColor);
					snipeButton.setBackground(black);
					orderTypeLabelComboitems.setEnabled(true);
					orderTypeLabelComboitems.setEnabled(true);
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
					gasModeComboitems.setEnabled(true);
					takeProfitOrderLimit.setEnabled(false);
					takeProfitOrderLimit.setEditable(false);
					
					minLiquidityText.setEditable(false);
					minLiquidityText.setEnabled(false);
					
					expectedTokensText.setEditable(false);
					expectedTokensText.setEnabled(false);
					
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