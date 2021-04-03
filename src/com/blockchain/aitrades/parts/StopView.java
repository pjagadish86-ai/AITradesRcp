package com.blockchain.aitrades.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class StopView {
	
	@PostConstruct
	public void createComposite(Composite ancestor) {
		ancestor.setLayout(new GridLayout(1, false));
		createTradeInput(ancestor);
	}

	private void createTradeInput(Composite tradeInputComposite) {
		
		Composite buttonComposite = new Composite(tradeInputComposite, SWT.NONE);
		GridLayout buttonlayout = new GridLayout(3, false);
		buttonlayout.marginTop = 20;
		buttonComposite.setLayout(buttonlayout);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label sideLabel = new Label(buttonComposite, SWT.NONE);
		sideLabel.setText("Side :");
		
		Button tradeButton = new Button(buttonComposite, SWT.PUSH);
		tradeButton.setText("Buy");
		Device device = Display.getCurrent( );
		RGB rgbGreen = new RGB(0, 240, 0);
		Color color = new Color(device, rgbGreen);
		tradeButton.setBackground(color);
		tradeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tradeButton.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event arg0) {
					}
				});
			}
		});

		Button sellButton = new Button(buttonComposite, SWT.PUSH);
		sellButton.setText("Sell");
		RGB rgbRed = new RGB(255, 0, 0);
		Color redColor = new Color(device, rgbRed);
		sellButton.setBackground(redColor);
		sellButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sellButton.addListener(SWT.Selection, new Listener() {
					@Override
					public void handleEvent(Event arg0) {
					}
				});
			}
		});
		
		createstopPrice(tradeInputComposite);
		createQuatity(tradeInputComposite);
		
	}

	private void createstopPrice(Composite tradeInputComposite) {
		Composite stopPriceComposite = new Composite(tradeInputComposite, SWT.NONE);
		GridLayout quatitylayout = new GridLayout(2, false);
		stopPriceComposite.setLayout(quatitylayout);
		stopPriceComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label stopPriceLabel = new Label(stopPriceComposite, SWT.NONE);
		stopPriceLabel.setText("Stop :");
		Text stopPriceText = new Text(stopPriceComposite, SWT.BORDER);
		stopPriceText.setLayoutData(new GridData(100, 10));
	}

	private void createQuatity(Composite tradeInputComposite) {
		Composite quatityComposite = new Composite(tradeInputComposite, SWT.NONE);
		GridLayout quatitylayout = new GridLayout(2, false);
		quatityComposite.setLayout(quatitylayout);
		quatityComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label fromtLabel = new Label(quatityComposite, SWT.NONE);
		fromtLabel.setText("Quantity :");
		Text fromText = new Text(quatityComposite, SWT.BORDER);
		fromText.setLayoutData(new GridData(100, 10));

	}

	
	
}
