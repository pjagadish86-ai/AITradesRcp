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

public class LimitView {
	
	@PostConstruct
	public void createComposite(Composite ancestor) {
		ancestor.setLayout(new GridLayout(1, false));
		createTradeInput(ancestor);
	}

	public void createTradeInput(Composite tradeInputComposite) {
		
		Composite buttonComposite = new Composite(tradeInputComposite, SWT.NONE);
		GridLayout buttonlayout = new GridLayout(3, false);
		buttonlayout.marginTop = 20;
		buttonComposite.setLayout(buttonlayout);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		
		createLimitPrice(tradeInputComposite);
		createQuatity(tradeInputComposite);
		
	}

	private void createLimitPrice(Composite tradeInputComposite) {
		Composite limitPriceComposite = new Composite(tradeInputComposite, SWT.NONE);
		GridLayout quatitylayout = new GridLayout(2, false);
		limitPriceComposite.setLayout(quatitylayout);
		limitPriceComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label limitPriceLabel = new Label(limitPriceComposite, SWT.NONE);
		limitPriceLabel.setText("Limit :");
		Text limitPriceText = new Text(limitPriceComposite, SWT.BORDER);
		limitPriceText.setLayoutData(new GridData(100, 10));
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
